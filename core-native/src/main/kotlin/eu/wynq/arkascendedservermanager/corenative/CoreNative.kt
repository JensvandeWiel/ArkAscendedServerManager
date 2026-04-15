package eu.wynq.arkascendedservermanager.corenative

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import java.io.File
import java.nio.file.Path

/**
 * Kotlin-facing bindings for the Rust-backed helpers that live in `core-native`.
 */
object CoreNativeBindings {
    const val RUST_LIBRARY_NAME: String = "core_native"
    private const val SEARCH_PATH_PROPERTY = "jna.library.path"
    private const val COMPOSE_RESOURCES_PROPERTY = "compose.application.resources.dir"
    private const val PACKAGED_NATIVE_SUBDIR = "native"

    fun load(searchPath: Path? = null): CoreNativeLibrary {
        resolveSearchPaths(searchPath).forEach {
            NativeLibrary.addSearchPath(RUST_LIBRARY_NAME, it)
        }

        return Native.load(RUST_LIBRARY_NAME, CoreNativeLibrary::class.java)
    }

    private fun resolveSearchPaths(searchPath: Path?): List<String> {
        val directPath = searchPath?.toAbsolutePath()?.toString()
        val jnaPath = System.getProperty(SEARCH_PATH_PROPERTY)
        val resourcesDir = System.getProperty(COMPOSE_RESOURCES_PROPERTY)
            ?.takeIf { it.isNotBlank() }
            ?.let(::File)
        val resourcesPath = resourcesDir?.absolutePath
        val resourcesNativePath = resourcesDir?.let { File(it, PACKAGED_NATIVE_SUBDIR).absolutePath }

        return listOfNotNull(directPath, jnaPath, resourcesPath, resourcesNativePath)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
    }
}

interface CoreNativeLibrary : Library {
    fun core_native_get_asa_version(exec_path: String): Pointer?
    fun core_native_free_string(raw: Pointer?)
}

object CoreNative {
    private val nativeLibrary: CoreNativeLibrary by lazy { CoreNativeBindings.load() }

    fun getAsaVersion(execPath: Path): String? {
        val nativeString = nativeLibrary.core_native_get_asa_version(execPath.toAbsolutePath().toString())
            ?: return null

        return try {
            nativeString.getString(0)
        } finally {
            nativeLibrary.core_native_free_string(nativeString)
        }
    }
}


