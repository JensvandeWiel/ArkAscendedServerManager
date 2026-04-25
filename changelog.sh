#!/usr/bin/env bash
set -euo pipefail

TAG="${1:-}"
if [[ -z "$TAG" ]]; then
  echo "Usage: $0 <tag>  (example: $0 v0.1.0)" >&2
  exit 1
fi

if ! git rev-parse -q --verify "${TAG}^{commit}" >/dev/null 2>&1; then
  echo "Error: tag/ref '${TAG}' not found." >&2
  exit 1
fi

PREVIOUS_TAG="$(git describe --tags --abbrev=0 "${TAG}^" 2>/dev/null || true)"
if [[ -n "$PREVIOUS_TAG" && "$PREVIOUS_TAG" != "$TAG" ]]; then
  FROM_REF="$PREVIOUS_TAG"
else
  FROM_REF="$(git rev-list --max-parents=0 "$TAG" | head -n 1)"
fi

RANGE="${FROM_REF}..${TAG}"
COMMITS_FILE="$(mktemp)"
trap 'rm -f "$COMMITS_FILE"' EXIT

ORIGIN_URL="$(git remote get-url origin 2>/dev/null || true)"
if [[ -z "$ORIGIN_URL" ]]; then
  REPO_WEB_URL=""
elif [[ "$ORIGIN_URL" =~ ^git@github\.com:(.+)\.git$ ]]; then
  REPO_WEB_URL="https://github.com/${BASH_REMATCH[1]}"
elif [[ "$ORIGIN_URL" =~ ^https://github\.com/(.+)\.git$ ]]; then
  REPO_WEB_URL="https://github.com/${BASH_REMATCH[1]}"
elif [[ "$ORIGIN_URL" =~ ^https://github\.com/(.+)$ ]]; then
  REPO_WEB_URL="https://github.com/${BASH_REMATCH[1]}"
else
  REPO_WEB_URL=""
fi

git log "$RANGE" --pretty=format:'%s%x09%h' --no-merges > "$COMMITS_FILE" || true

echo "## ${TAG}"
echo

if [[ ! -s "$COMMITS_FILE" ]]; then
  echo "No changelog entries available for this release."
  exit 0
fi

awk -F '\t' -v repo_url="$REPO_WEB_URL" '
  function heading_for(x) {
    if (x == "feat") return "Feature"
    if (x == "feature") return "Feature"
    if (x == "fix") return "Fix"
    if (x == "chore") return "Chore"
    if (x == "docs") return "Docs"
    if (x == "refactor") return "Refactor"
    return "None"
  }

  function is_allowed(x) { return (x=="feat" || x=="fix" || x=="chore" || x=="docs" || x=="feature" || x=="refactor") }

  {
    subject=$1
    sha=$2
    prefix="none"
    display_subject=subject

    if (subject ~ /^[A-Za-z0-9_-]+(\([^)]+\))?!?:[[:space:]]+/) {
      detected=subject
      sub(/\(.*/, "", detected)
      sub(/!.*/, "", detected)
      sub(/:.*/, "", detected)
      detected=tolower(detected)

      if (is_allowed(detected)) {
        prefix=detected
        display_subject=subject
        sub(/^[A-Za-z0-9_-]+(\([^)]+\))?!?:[[:space:]]+/, "", display_subject)
      } else {
        prefix="none"
      }
    }

    if (repo_url != "") {
      groups[prefix]=groups[prefix] sprintf("- %s ([%s](%s/commit/%s))\n", display_subject, sha, repo_url, sha)
    } else {
      groups[prefix]=groups[prefix] sprintf("- %s (%s)\n", display_subject, sha)
    }
  }
  END {
    order[1]="feat"; order[2]="fix"; order[3]="chore"; order[4]="none"
    for (i=1; i<=4; i++) {
      key=order[i]
      if (groups[key] != "") {
        printf("### %s\n\n%s\n", heading_for(key), groups[key])
      }
    }
  }
' "$COMMITS_FILE"