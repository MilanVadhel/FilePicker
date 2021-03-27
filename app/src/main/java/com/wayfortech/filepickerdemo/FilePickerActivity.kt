package com.wayfortech.filepickerdemo

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.wayfortech.filepickerdemo.enum.FileType.*
import com.wayfortech.filepickerdemo.utils.Constants
import com.wayfortech.filepickerdemo.utils.Constants.FILE_TYPE
import com.wayfortech.filepickerdemo.utils.Constants.MIME_AUDIO
import com.wayfortech.filepickerdemo.utils.Constants.MIME_IMAGE
import com.wayfortech.filepickerdemo.utils.Constants.MIME_VIDEO
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FilePickerActivity : AppCompatActivity() {

    private val TAG = "FilePickerActivity"
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    lateinit var imageFileUri: Uri
    lateinit var videoFileUri: Uri

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)
        setUpDialog()
        requestPermission()
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

    private fun pickVideo() {
        alertDialogBuilder.setTitle(R.string.pick_video)
        alertDialogBuilder.setItems(
            Constants.videoOptions,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                    if (position == 0) {
                        captureVideoFromCamera()
                    } else {
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

    private fun pickAudio() {
        alertDialogBuilder.setTitle(R.string.pick_audio)
        alertDialogBuilder.setItems(
            Constants.audioOptions,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, position: Int) {
                    if (position == 0) {
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

    val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageFileUri.let {
                val intent = Intent()
                intent.putExtra("FILE", it)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun captureImageFromCamera() {
        captureImage.launch(createImageFileAndGetFileUri())
    }

    private fun createImageFileAndGetFileUri(): Uri {
        val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        val fileName = "Image_$timeStamp"
        val storageDir = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Environment.getStorageDirectory()
        } else {
            applicationContext.filesDir
        }
        val imageFile = File.createTempFile(fileName, ".jpg", storageDir)
        imageFileUri = FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName + ".provider", imageFile
        )
        return imageFileUri
    }

    val captureVideo = registerForActivityResult(ActivityResultContracts.TakeVideo()) {
        videoFileUri.let {
            val intent = Intent()
            intent.putExtra("FILE", it)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun captureVideoFromCamera() {
        captureVideo.launch(createVideoFileAndGetFileUri())
    }

    private fun createVideoFileAndGetFileUri(): Uri {
        val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        val fileName = "Video_$timeStamp"
        val storageDir = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Environment.getStorageDirectory()
        } else {
            applicationContext.filesDir
        }
        val videoFile = File.createTempFile(fileName, ".mp4", storageDir)
        if (videoFile != null) {
            videoFileUri = FileProvider.getUriForFile(
                applicationContext,
                applicationContext.packageName + ".provider", videoFile
            )
        }
        return videoFileUri
    }

    private fun getContent(mimeType: String) {
        when (mimeType) {
            MIME_IMAGE -> getContent.launch(MIME_IMAGE)
            MIME_VIDEO -> getContent.launch(MIME_VIDEO)
            MIME_AUDIO -> getContent.launch(MIME_AUDIO)
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val intent = Intent()
                intent.putExtra("FILE", uri)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

}