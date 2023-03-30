package com.goormthon.mang9rme.kimbsu.data

enum class SelectPhotoStatus {
    /**
     * 디폴트 상태 값
     */
    NONE,

    /**
     * 카메라로부터 사진을 가져오는 경우
     */
    CAMERA,

    /**
     * 갤러리로부터 사진을 가져오는 경우
     */
    GALLERY,

    /**
     * 사진 가져오기 성공
     */
    SUCCESS
}