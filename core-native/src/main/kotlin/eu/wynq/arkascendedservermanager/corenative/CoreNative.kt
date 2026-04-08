package eu.wynq.arkascendedservermanager.corenative

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import java.nio.file.Path

/**
 * Kotlin-facing bindings for the Rust-backed helpers that live in `core-native`.
 */
object CoreNativeBindings {
    const val RUST_LIBRARY_NAME: String = "core_native"
    private const val SEARCH_PATH_PROPERTY = "jna.library.path"

    fun load(searchPath: Path? = null): CoreNativeLibrary {
        val path = searchPath?.toAbsolutePath()?.toString()
            ?: System.getProperty(SEARCH_PATH_PROPERTY)

        if (!path.isNullOrBlank()) {
            NativeLibrary.addSearchPath(RUST_LIBRARY_NAME, path)
        }

        return Native.load(RUST_LIBRARY_NAME, CoreNativeLibrary::class.java)
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


