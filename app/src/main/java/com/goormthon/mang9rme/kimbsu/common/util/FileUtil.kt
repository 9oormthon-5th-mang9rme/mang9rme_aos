package com.goormthon.mang9rme.kimbsu.common.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FileUtil(
    private val mContext: Context
) {

    private val cachePath: String = mContext.cacheDir.absolutePath

    private fun getFileName(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyyMMddhhmmss")
        val strDate = format.format(calendar.time)

        return "mang9rme_$strDate"
    }

    /**
     * 이미지 저장을 위한 Temp 파일 생성 함수
     */
    fun createImageFile(): File? {
        return try {
            val imgFileName = getFileName()
            DLog.d(TAG, "imgFileName=$imgFileName")
            val albumFile = getAlbumDir()
            DLog.d(TAG, "albumFile=$albumFile")
            File.createTempFile(imgFileName, ".jpg", albumFile)
        } catch (e: Exception) {
            null
        }
    }

    private fun getAlbumDir(): File? {
        var storageDir: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            storageDir = getAlbumStorageDir("mang9rme")
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        DLog.d("CameraSample", "failed to create directory")
                        return null
                    }
                }
            }
        } else {
            DLog.d(javaClass.simpleName, "External storage is not mounted READ/WRITE.")
        }
        return storageDir
    }

    /**
     * 폴더 생성
     */
    private fun getAlbumStorageDir(albumName: String): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            albumName
        )
    }

    /**
     * File을 캐시 폴더로 복사하는 함수
     */
    @Throws(IOException::class)
    fun copyFileToCacheFolder(strOriginPath: String): String {
        val strFilePath: String = cachePath + "/" + getFileName() + ".jpg"
        val fileOrigin = File(strOriginPath)
        val fileCopy = File(strFilePath)
        val inOrigin = FileInputStream(fileOrigin)
        val buf = ByteArray(inOrigin.available())
        var idx = 0
        while (idx < buf.size) {
            val read = inOrigin.read(buf, idx, buf.size - idx)
            if (read < 0) throw EOFException("File COPY ERROR!!")
            idx += read
        }
        inOrigin.close()
        val outCopy = FileOutputStream(fileCopy)
        outCopy.write(buf)
        outCopy.close()
        return fileCopy.absolutePath
    }

    /**
     * 이미지 파일을 캐시로 복사하는 함수
     *
     * @param uri      캐시로 복사할 이미지의 Content Uri
     *
     * @return 캐시로 복사한 이미지 파일의 절대 경로를 반환함
     */
    @Throws(IOException::class)
    fun copyFileToCacheFolder(contentUri: Uri): String {
        if (contentUri.scheme != "content")
            return copyFileToCacheFolder(contentUri.path!!)

        val filePath = cachePath + "/" + getFileName() + ".jpg"
        val fileCopy = File(filePath)

        val inOrigin = mContext.contentResolver.openInputStream(contentUri)
        val buf = ByteArray(inOrigin!!.available())

        var idx = 0
        while (idx < buf.size) {
            val read = inOrigin.read(buf, idx, buf.size - idx)
            if (read < 0) throw EOFException("File COPY ERROR!!")
            idx += read
        }
        inOrigin.close()

        val outCopy = FileOutputStream(fileCopy)
        outCopy.write(buf)
        outCopy.close()

        return fileCopy.absolutePath
    }

    /**
     * 비트맵 이미지를 캐시 메모리에 저장하는 함수
     *
     * @param bitmap      이미지 파일로 저장할 비트맵 객체
     * @param strFileName 이미지 파일명
     */
    fun saveBitmapToCacheFolder(bitmap: Bitmap, strFileName: String) {
        val strFilePath: String = "$cachePath/$strFileName"
        val fileCacheItem = File(strFilePath)
        if (fileCacheItem.exists()) deleteFile(strFilePath)
        try {
            fileCacheItem.createNewFile()
            val out: OutputStream = FileOutputStream(fileCacheItem)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 파일 삭제
     */
    fun deleteFile(path: String) {
        val file = File("$cachePath/$path")
        if (file != null) {
            if (file.exists()) file.delete()
        }
    }

    companion object {
        private const val TAG: String = "FileUtil"
    }

}