package com.goormthon.mang9rme.kimbsu.feature.enroll.repository

import android.app.Application
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.goormthon.mang9rme.kimbsu.common.util.DLog
import com.goormthon.mang9rme.kimbsu.common.util.FileUtil
import com.goormthon.mang9rme.kimbsu.common.util.ImageUtil
import com.goormthon.mang9rme.kimbsu.feature.enroll.data.UploadImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.ParsePosition
import java.text.SimpleDateFormat

class EnrollImageRepository(
    private val application: Application
) {

    private val fileUtil: FileUtil = FileUtil(application)
    private val imageUtil: ImageUtil = ImageUtil(application)

    private val exifDateFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy:MM:dd HH:mm:ss")
    }
    private val dateFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat("MM월 dd일 E요일")
    }

    /**
     * 카메라 앱에서 찍은 이미지의 원본을 저장하기 위한 임시파일을 생성하는 함수
     *
     * @return 임시 파일을 성공적으로 만들었다면 해당 파일 객체를 리턴하고, 그렇지 않다면 null을 리턴함
     */
    fun makeTempFileForCamera(): File? {
        return try {
            fileUtil.createImageFile()
        } catch (e: IOException) {
            null
        }
    }

    /**
     * 파일을 삭제하는 함수
     *
     * @param pFilePath 삭제할 파일의 절대경로
     */
    suspend fun requestDeleteFile(pFilePath: String) {
        withContext(Dispatchers.IO) {
            val isDeleted = File(pFilePath).delete()
            if (!isDeleted)
                DLog.e("${TAG}_requestDeleteFile", "File is not Deleted!, filePath=$pFilePath")
        }
    }

    /**
     * 이미지 파일을 캐시에 적재한 후, 최대 2048*2048 사이즈의 비트맵으로 변환하는 함수
     *
     * @param pFile     이미지 파일 객체
     *
     * @return 후처리가 끝난 이미지 데이터
     */
    suspend fun requestImagePostProcess(pFile: File): UploadImageData {
        return withContext(Dispatchers.IO) {
            val cachePath = fileUtil.copyFileToCacheFolder(pFile.absolutePath)

            var imgLat: Float? = null
            var imgLng: Float? = null
            var imgCreateDate: String? = null
            try {
                val exif = ExifInterface(pFile)

                imgLat = exif.latLong?.get(0)?.toFloat()
                imgLng = exif.latLong?.get(1)?.toFloat()

                exif.getAttribute(ExifInterface.TAG_DATETIME)?.let { dateTime ->
                    exifDateFormatter.parse(dateTime, ParsePosition(0))?.let { date ->
                        imgCreateDate = dateFormatter.format(date)
                    }
                }
            } catch (e: Exception) {
                DLog.e(TAG, "Can not read ExifFile from ${pFile.absolutePath}")
            }

            val originImgSize = IntArray(2) { 0 }
            val bmp2048 = imageUtil.createResizeBitmap(cachePath, 2048, originImgSize)

            val fileName = cachePath.substring(cachePath.lastIndexOf("/") + 1)
            UploadImageData(fileName, cachePath, bmp2048, imgLat, imgLng, imgCreateDate)
        }
    }

    /**
     * contentUri에 대응되는 이미지 파일을 캐시 메모리에 적재한 후, 최대 2048*2048 사이즈의 비트맵으로 변환하는 함수
     *
     * @param pContentUri 이미지 파일의 content uri
     *
     * @return 후처리가 끝난 이미지 데이터
     */
    suspend fun requestImagePostProcess(pContentUri: Uri): UploadImageData {
        return withContext(Dispatchers.IO) {
            var imgLat: Float? = null
            var imgLng: Float? = null
            var imgCreateDate: String? = null
            application.contentResolver.openInputStream(pContentUri)?.let { inputStream ->
                try {
                    val exif = ExifInterface(inputStream)

                    imgLat = exif.latLong?.get(0)?.toFloat()
                    imgLng = exif.latLong?.get(1)?.toFloat()

                    exif.getAttribute(ExifInterface.TAG_DATETIME)?.let { dateTime ->
                        exifDateFormatter.parse(dateTime, ParsePosition(0))?.let { date ->
                            imgCreateDate = dateFormatter.format(date)
                        }
                    }
                } catch (e: IOException) {
                    DLog.e(TAG, "Can not read ExifFile from $pContentUri")
                }
                inputStream.close()
            }

            val cachePath = fileUtil.copyFileToCacheFolder(pContentUri)
            val originImgSize = IntArray(2) { 0 }
            val bmp2048 = imageUtil.createResizeBitmap(cachePath, 2048, originImgSize)

            val fileName = cachePath.substring(cachePath.lastIndexOf("/") + 1)
            UploadImageData(fileName, cachePath, bmp2048, imgLat, imgLng, imgCreateDate)
        }
    }

    companion object {
        private const val TAG: String = "EnrollImageRepository"
    }

}