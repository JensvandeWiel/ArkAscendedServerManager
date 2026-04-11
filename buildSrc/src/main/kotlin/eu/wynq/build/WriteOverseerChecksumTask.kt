package eu.wynq.build

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.security.MessageDigest

@CacheableTask
abstract class WriteOverseerChecksumTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val executableFile: RegularFileProperty

    @get:OutputFile
    abstract val checksumFile: RegularFileProperty

    @TaskAction
    fun writeChecksum() {
        val executable = executableFile.get().asFile
        check(executable.exists()) { "Expected overseer executable at ${executable.absolutePath}" }

        val digest = MessageDigest.getInstance("SHA-256")
        executable.inputStream().use { input ->
            val buffer = ByteArray(8192)
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                digest.update(buffer, 0, read)
            }
        }

        val checksum = digest.digest().joinToString("") { "%02x".format(it) }
        val outputFile = checksumFile.get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(checksum)
    }
}

