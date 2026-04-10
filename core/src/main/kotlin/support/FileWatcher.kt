package eu.wynq.arkascendedservermanager.core.support

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import java.io.File
import java.io.RandomAccessFile

fun watchFileContent(file: File, pollIntervalMs: Long = 500L): Flow<String> = flow {
    var lastPosition = 0L

    if (file.exists()) {
        file.useLines { lines ->
            lines.forEach { emit(it) }
        }
        lastPosition = file.length()
    }

    while (currentCoroutineContext().isActive) {
        if (file.exists()) {
            val currentLength = file.length()

            if (currentLength < lastPosition) {
                lastPosition = 0L
            } else if (currentLength > lastPosition) {
                RandomAccessFile(file, "r").use { raf ->
                    raf.seek(lastPosition)

                    var line: String? = raf.readLine()
                    while (line != null) {
                        val utf8Line = String(line.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)

                        emit(utf8Line)

                        lastPosition = raf.filePointer
                        line = raf.readLine()
                    }
                }
            }
        }

        delay(pollIntervalMs)
    }
}.flowOn(Dispatchers.IO)