package com.goormthon.mang9rme.kimbsu.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

class ImageUtil(
    private val mContext: Context
) {

    /**
     * 이미지 파일의 크기가 maxPixel * maxPixel 보다 큰 경우, 비트맵을 리사이징해서 photoPath에 저장함
     *
     * @param photoPath     리사이징한 비트맵을 저장할 경로
     * @param maxPixel      픽셀 최대값
     * @param originImgSize 원본 이미지의 크기를 저장하는 버퍼, {(0, width), (1, height)}
     * @return 리사이징이 필요하다면 리사이징된 비트맵, 그렇지 않다면 원본 이미지의 비트맵
     */
    fun createResizeBitmap(photoPath: String, maxPixel: Int, originImgSize: IntArray): Bitmap? {
        if (maxPixel > 0) {
            // Get the dimensions of the bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(
                photoPath,
                options
            ) // inJustDecodeBounds 설정을 해주지 않으면 이부분에서 큰 이미지가 들어올 경우 outofmemory Exception이 발생한다.
            var resizedBitmap: Bitmap? = null
            var photoW = options.outWidth
            var photoH = options.outHeight
            originImgSize[0] = photoW
            originImgSize[1] = photoH
            val photoPixel = if (photoW > photoH) photoW else photoH
            options.inJustDecodeBounds = false
            if (photoPixel > maxPixel) {
                val bitmap = BitmapFactory.decodeFile(photoPath, options)
                val ratio = photoPixel.toFloat() / maxPixel
                photoW /= ratio.toInt()
                photoH /= ratio.toInt()
                resizedBitmap = Bitmap.createScaledBitmap(bitmap, photoW, photoH, true)
            } else {
                resizedBitmap = BitmapFactory.decodeFile(photoPath, options)
            }
            return try {
                var rotateBitmap: Bitmap? = null
                val strFileName = photoPath.substring(photoPath.lastIndexOf("/") + 1)
                var exifDegree = 0f
                try {
                    val exif = ExifInterface(photoPath)
                    val exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )
                    exifDegree = exifOrientationToDegrees(exifOrientation)
                    if (exifDegree != 0f && resizedBitmap != null) {
                        val m = Matrix()
                        m.setRotate(
                            exifDegree,
                            resizedBitmap.width.toFloat() / 2,
                            resizedBitmap.height.toFloat() / 2
                        )
                        var intRotateWidth = 0
                        var intRotateHeight = 0
                        if (exifDegree == 90f ||
                            exifDegree == 270f
                        ) {
                            intRotateWidth = resizedBitmap.height
                            intRotateHeight = resizedBitmap.width
                        } else {
                            intRotateWidth = resizedBitmap.width
                            intRotateHeight = resizedBitmap.height
                        }
                        try {
                            var converted = Bitmap.createBitmap(
                                resizedBitmap,
                                0,
                                0,
                                resizedBitmap.width,
                                resizedBitmap.height,
                                m,
                                true
                            )
                            converted = Bitmap.createScaledBitmap(
                                converted!!,
                                intRotateWidth,
                                intRotateHeight,
                                true
                            )
                            rotateBitmap = converted
                        } catch (ex: OutOfMemoryError) {
                            // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
                        }
                    } else rotateBitmap = resizedBitmap
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (rotateBitmap != null) {
                    val fileUtil = FileUtil(mContext)
                    fileUtil.saveBitmapToCacheFolder(rotateBitmap, strFileName)
                    rotateBitmap
                } else null
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    private fun exifOrientationToDegrees(exifOrientation: Int): Float {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90f
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180f
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270f
        }
        return 0f
    }

}