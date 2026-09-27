package com.example.altu.Profile

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

class AvatarStore(
    context: Context,
) {
    private val folder = File(context.applicationContext.filesDir, FOLDER).apply { mkdirs() }
    private val resolver = context.applicationContext.contentResolver

    fun save(from: Uri): String? {
        val dest = File(folder, "${UUID.randomUUID()}.jpg")
        return try {
            resolver.openInputStream(from)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            } ?: return null
            dest.absolutePath
        } catch (_: Exception) {
            dest.delete()
            null
        }
    }

    fun delete(path: String?) {
        if (path.isNullOrBlank()) return
        File(path).delete()
    }

    fun deleteAll() {
        folder.listFiles()?.forEach { it.delete() }
    }

    companion object {
        private const val FOLDER = "avatars"
    }
}
