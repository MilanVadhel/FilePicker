package com.wayfortech.filepickerdemo

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wayfortech.filepickerdemo.enum.FileType.*
import com.wayfortech.filepickerdemo.utils.Constants
import com.wayfortech.filepickerdemo.utils.Constants.CAMERA_IMAGE_REQUEST_CODE
import com.wayfortech.filepickerdemo.utils.Constants.CAMERA_VIDEO_REQUEST_CODE
import com.wayfortech.filepickerdemo.utils.Constants.FILE_TYPE
import com.wayfortech.filepickerdemo.utils.Constants.GALLERY_IMAGE_REQUEST_CODE
import com.wayfortech.filepickerdemo.utils.Constants.GALLERY_VIDEO_REQUEST_CODE
import com.wayfortech.filepickerdemo.utils.Constants.MIME_AUDIO
import com.wayfortech.filepickerdemo.utils.Constants.MIME_IMAGE
import com.wayfortech.filepickerdemo.utils.Constants.MIME_VIDEO
import com.wayfortech.filepickerdemo.utils.Constants.RECORD_AUDIO_REQUEST_CODE
import com.wayfortech.filepickerdemo.utils.Constants.STORAGE_AUDIO_REQUEST_CODE
import java.io.File


class FilePickerActivity : AppCompatActivity() {

    private val TAG = "FilePickerActivity"
    private lateinit var alertDialogBuilder: AlertDialog.Builder

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.entries.all {
                    it.value == true
                }) {
                checkWhichFile(intent.extras?.get(FILE_TYPE)?.toString())
            } else {
                finish()
            }
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val intent = Intent()
                intent.putExtra("FILE", getFileFromUri(uri))
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)
        setUpDialog()
        requestPermission()
        //checkWhichFile(intent.extras?.get(FILE_TYPE)?.toString())
    }

    private fun requestPermission() {
        requestMultiplePermission.launch(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    private fun setUpDialog() {
        alertDialogBuilder = AlertDialog.Builder(this)
    }

    private fun checkWhichFile(fileType: String?) {
        fileType?.let {
            if (it.equals(IMAGE.name)) {
                pickImage()
            } else if (it.equals(VIDEO.name)) {
                pickVideo()
            } else if (it.equals(AUDIO.name)) {
                pickAudio()
            } else if (it.equals(DOCUMENT.name)) {
                pickDocument()
            } else {

            }
        }
    }

    private fun pickImage() {
        alertDialogBuilder.setTitle(R.string.pick_image)
        alertDialogBuilder.setItems(
            Constants.imageOptions,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                    if (position == 0) {
                        captureImageFromCamera()
                    } else {
                        //selectImageFromGallery()
                        getContent(MIME_IMAGE)
                    }
                }
            })
        alertDialogBuilder.setNegativeButton(
            R.string.cancel,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                    dialogInterface?.dismiss()
                    finish()
                }

            })
        alertDialogBuilder.setOnCancelListener {
            finish()
        }
        alertDialogBuilder.show()
    }

    private fun captureImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST_CODE)
    }

    private fun selectImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_IMAGE_REQUEST_CODE)
    }

    private fun pickVideo() {
        alertDialogBuilder.setTitle(R.string.pick_video)
        alertDialogBuilder.setItems(
            Constants.videoOptions,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                    if (position == 0) {
                        captureVideoFromCamera()
                    } else {
                        //selectVideoFromGallery()
                        getContent(MIME_VIDEO)
                    }
                }
            })
        alertDialogBuilder.setNegativeButton(
            R.string.cancel,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                    dialogInterface?.dismiss()
                    finish()
                }

            })
        alertDialogBuilder.setOnCancelListener {
            finish()
        }
        alertDialogBuilder.show()
    }

    private fun captureVideoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_VIDEO_REQUEST_CODE)
    }

    private fun selectVideoFromGallery() {
        val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(cameraIntent, GALLERY_VIDEO_REQUEST_CODE)
    }

    private fun pickAudio() {
        alertDialogBuilder.setTitle(R.string.pick_audio)
        alertDialogBuilder.setItems(
            Constants.audioOptions,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                    if (position == 0) {
                        recordAudio()
                    } else {
                        //selectAudioFromStorage()
                        getContent(MIME_AUDIO)
                    }
                }
            })
        alertDialogBuilder.setNegativeButton(
            R.string.cancel,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, p1: Int) {
                    dialogInterface?.dismiss()
                    finish()
                }

            })
        alertDialogBuilder.setOnCancelListener {
            finish()
        }
        alertDialogBuilder.show()
    }

    private fun recordAudio() {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        startActivityForResult(intent, RECORD_AUDIO_REQUEST_CODE)
    }

    private fun selectAudioFromStorage() {
        val storageIntent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(storageIntent, STORAGE_AUDIO_REQUEST_CODE)
    }

    private fun pickDocument() {
        TODO("Not yet implemented")
    }

    private fun getContent(mimeType: String) {
        when (mimeType) {
            MIME_IMAGE -> getContent.launch(MIME_IMAGE)
            MIME_VIDEO -> getContent.launch(MIME_VIDEO)
            MIME_AUDIO -> getContent.launch(MIME_AUDIO)
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Files.FileColumns.DATA)
        var file: File? = null
        val contentResolver = applicationContext.contentResolver
        val cursor = contentResolver.query(
            uri,
            filePathColumn, null, null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val path = cursor.getString(columnIndex)
            file = File(path)
            cursor.close()
        }
        return file
    }

    fun getPath(uri: Uri?, context: Context): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(column_index)
            cursor.close()
            path
        } else {
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                CAMERA_IMAGE_REQUEST_CODE -> {
                    setResult(RESULT_OK, data)
                    finish()
                }
                GALLERY_IMAGE_REQUEST_CODE -> {
                    setResult(RESULT_OK, data)
                    finish()
                }
                CAMERA_VIDEO_REQUEST_CODE -> {
                    setResult(RESULT_OK, data)
                    finish()
                }
                GALLERY_VIDEO_REQUEST_CODE -> {
                    setResult(RESULT_OK, data)
                    finish()
                }
                STORAGE_AUDIO_REQUEST_CODE -> {
                    setResult(RESULT_OK, data)
                    finish()
                }
                RECORD_AUDIO_REQUEST_CODE -> {
                    setResult(RESULT_OK, data)
                    finish()
                }
            }
        }
    }
}