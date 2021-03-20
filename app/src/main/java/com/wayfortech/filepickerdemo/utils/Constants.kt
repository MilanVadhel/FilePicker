package com.wayfortech.filepickerdemo.utils

object Constants {
    const val FILE_TYPE = "FILE_TYPE"
    const val FILE_PICKER_REQUEST_CODE = 100
    const val CAMERA_IMAGE_REQUEST_CODE = 101
    const val GALLERY_IMAGE_REQUEST_CODE = 102
    const val CAMERA_VIDEO_REQUEST_CODE = 103
    const val GALLERY_VIDEO_REQUEST_CODE = 104
    const val RECORD_AUDIO_REQUEST_CODE = 105
    const val STORAGE_AUDIO_REQUEST_CODE = 106


    val imageOptions = arrayOf("Capture image from camera", "Select image from gallery")
    val videoOptions = arrayOf("Capture video from camera", "Select video from gallery")
    val audioOptions = arrayOf("Record Audio", "Select audio from storage")

    const val MIME_IMAGE = "image/*"
    const val MIME_VIDEO = "video/*"
    const val MIME_AUDIO = "audio/*"
}