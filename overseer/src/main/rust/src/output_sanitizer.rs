#[derive(Clone, Copy)]
enum EscapeState {
    Ground,
    Escape,
    Csi,
    Osc,
    OscEscape,
}

pub struct OutputSanitizer {
    state: EscapeState,
    pending_cr: bool,
    consecutive_newlines: usize,
}

impl OutputSanitizer {
    pub fn new() -> Self {
        Self {
            state: EscapeState::Ground,
            pending_cr: false,
            consecutive_newlines: 0,
        }
    }

    pub fn filter(&mut self, input: &[u8]) -> Vec<u8> {
        let mut out = Vec::with_capacity(input.len());

        for &byte in input {
            if self.pending_cr {
                if byte == b'\n' {
                    self.push_newline(&mut out);
                    self.pending_cr = false;
                    continue;
                }

                self.push_newline(&mut out);
                self.pending_cr = false;
            }

            match self.state {
                EscapeState::Ground => {
                    if byte == 0x1b {
                        self.state = EscapeState::Escape;
                    } else if byte == b'\r' {
                        self.pending_cr = true;
                    } else if byte == b'\n' {
                        self.push_newline(&mut out);
                    } else if is_loggable_byte(byte) {
                        out.push(byte);
                        self.consecutive_newlines = 0;
                    }
                }
                EscapeState::Escape => {
                    if byte == b'[' {
                        self.state = EscapeState::Csi;
                    } else if byte == b']' {
                        self.state = EscapeState::Osc;
                    } else if (0x40..=0x5f).contains(&byte) {
                        self.state = EscapeState::Ground;
                    } else {
                        self.state = EscapeState::Ground;
                        if byte == b'\r' {
                            self.pending_cr = true;
                        } else if byte == b'\n' {
                            self.push_newline(&mut out);
                        } else if is_loggable_byte(byte) {
                            out.push(byte);
                            self.consecutive_newlines = 0;
                        }
                    }
                }
                EscapeState::Csi => {
                    if (0x40..=0x7e).contains(&byte) {
                        self.state = EscapeState::Ground;
                    }
                }
                EscapeState::Osc => {
                    if byte == 0x07 {
                        self.state = EscapeState::Ground;
                    } else if byte == 0x1b {
                        self.state = EscapeState::OscEscape;
                    }
                }
                EscapeState::OscEscape => {
                    if byte == b'\\' {
                        self.state = EscapeState::Ground;
                    } else if byte != 0x1b {
                        self.state = EscapeState::Osc;
                    }
                }
            }
        }

        out
    }

    pub fn finish(&mut self) -> Vec<u8> {
        let mut out = Vec::new();
        if self.pending_cr {
            self.push_newline(&mut out);
            self.pending_cr = false;
        }
        out
    }

    fn push_newline(&mut self, out: &mut Vec<u8>) {
        // Allow at most one blank line between content blocks.
        if self.consecutive_newlines < 2 {
            out.push(b'\n');
        }
        self.consecutive_newlines += 1;
    }
}

fn is_loggable_byte(byte: u8) -> bool {
    matches!(byte, b'\t') || (byte >= 0x20 && byte != 0x7f)
}

#[cfg(test)]
mod tests {
    use super::OutputSanitizer;

    #[test]
    fn strips_ansi_and_non_printable_control_bytes() {
        let mut sanitizer = OutputSanitizer::new();
        let input = b"prefix\x1b[31mred\x1b[0m\n\x08\x00suffix";

        let cleaned = sanitizer.filter(input);
        assert_eq!(cleaned, b"prefixred\nsuffix");
    }

    #[test]
    fn handles_ansi_sequences_split_across_chunks() {
        let mut sanitizer = OutputSanitizer::new();

        let part1 = sanitizer.filter(b"before \x1b[");
        let part2 = sanitizer.filter(b"32mgreen\x1b[0");
        let part3 = sanitizer.filter(b"m after\n");

        let mut combined = Vec::new();
        combined.extend_from_slice(&part1);
        combined.extend_from_slice(&part2);
        combined.extend_from_slice(&part3);

        assert_eq!(combined, b"before green after\n");
    }

    #[test]
    fn normalizes_cr_and_crlf_without_merging_lines() {
        let mut sanitizer = OutputSanitizer::new();

        let part1 = sanitizer.filter(b"Loading...\r");
        let part2 = sanitizer.filter(b"Added\r\nDone\n");

        let mut combined = Vec::new();
        combined.extend_from_slice(&part1);
        combined.extend_from_slice(&part2);

        assert_eq!(combined, b"Loading...\nAdded\nDone\n");
    }

    #[test]
    fn collapses_excessive_blank_lines() {
        let mut sanitizer = OutputSanitizer::new();
        let cleaned = sanitizer.filter(b"A\n\n\n\nB\r\r\rC\n\n\nD\n");

        assert_eq!(cleaned, b"A\n\nB\n\nC\n\nD\n");
    }
}
