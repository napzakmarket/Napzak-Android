package com.napzak.market.remote

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.provider.MediaStore.MediaColumns.SIZE
import android.util.Size
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class ImageCompressor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolver: ContentResolver,
) {
    suspend fun compressImage(uri: Uri): Uri = withContext(Dispatchers.IO) {
        val (fileName, fileSize) = getImageMetaData(uri)
        val source = ImageDecoder.createSource(contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true

            val targetSize = calculateTargetSize(info.size.width, info.size.height)
            decoder.setTargetSize(targetSize.width, targetSize.height)
        }

        val compressedImage = ByteArrayOutputStream().use { buffer ->
            val quality = calculateQuality(fileSize)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, buffer)
            bitmap.recycle()
            buffer.toByteArray()
        }

        val cacheFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}_$fileName")

        cacheFile.outputStream()
            .use { it.write(compressedImage) }

        cacheFile.toUri()
    }

    private fun getImageMetaData(uri: Uri): Pair<String, Long> {
        var fileName = "unknown.jpg"
        var size = -1L

        contentResolver.query(
            uri,
            arrayOf(DISPLAY_NAME, SIZE),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndexOrThrow(DISPLAY_NAME)) ?: fileName
                size = cursor.getLong(cursor.getColumnIndexOrThrow(SIZE))
            }
        }

        return fileName to size
    }

    private fun calculateTargetSize(width: Int, height: Int): Size {
        if (width <= MAX_WIDTH && height <= MAX_HEIGHT) return Size(width, height)

        val scaleFactor = (MAX_WIDTH / width.toFloat()).coerceAtMost(MAX_HEIGHT / height.toFloat())
        return Size((width * scaleFactor).toInt(), (height * scaleFactor).toInt())
    }

    private fun calculateQuality(fileSize: Long): Int = when {
        fileSize > 3 * MAX_FILE_SIZE -> 75
        fileSize > 1 * MAX_FILE_SIZE -> 80
        else -> 90
    }

    fun clearCachedImage() = context.cacheDir.listFiles()
        ?.filter { it.name.startsWith("compressed_") }
        ?.forEach { it.delete() }

    companion object {
        private const val MAX_WIDTH = 1024
        private const val MAX_HEIGHT = 1024
        private const val MAX_FILE_SIZE = 1024 * 1024 // 1MB
    }
}
