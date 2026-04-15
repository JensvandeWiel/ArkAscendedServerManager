package support

import com.sun.jna.Memory
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Version
import com.sun.jna.platform.win32.Win32Exception
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun getExeVersion(filePath: String): io.github.z4kn4fein.semver.Version? {
    val versionApi = Version.INSTANCE
    val size = versionApi.GetFileVersionInfoSize(filePath, null)

    if (size == 0) return null

    val buffer = Memory(size.toLong())
    if (!versionApi.GetFileVersionInfo(filePath, 0, size, buffer)) {
        throw Win32Exception(Kernel32.INSTANCE.GetLastError())
    }

    val pointer = PointerByReference()
    val len = IntByReference()

    val success = versionApi.VerQueryValue(buffer, "\\", pointer, len)
    if (!success) return null

    val versionPtr = pointer.value
    val bytes = ByteArray(16)
    versionPtr.read(0, bytes, 0, 16)
    
    val bb = ByteBuffer.wrap(bytes, 8, 8).order(ByteOrder.LITTLE_ENDIAN)
    val dwFileVersionMS = bb.int
    val dwFileVersionLS = bb.int

    val v1 = (dwFileVersionMS ushr 16) and 0xffff
    val v2 = dwFileVersionMS and 0xffff
    val v3 = (dwFileVersionLS ushr 16) and 0xffff

    return io.github.z4kn4fein.semver.Version(v1, v2, v3)
}