package com.wayfortech.filepickerdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.wayfortech.filepickerdemo.enum.FileType
import com.wayfortech.filepickerdemo.utils.Constants.FILE_PICKER_REQUEST_CODE
import com.wayfortech.filepickerdemo.utils.Constants.FILE_TYPE
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "Permission is Granted ")
            } else {
                Log.d(TAG, "Permission is Denied ")
            }
        }

    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.entries.forEach {
                if (it.value) {
                    Log.d(TAG, "${it.key} : ${it.value} ")
                }
            }
        }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            Log.d(TAG, "Take Picture ")
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {

    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) {

    }

    private val pickAudio = registerForActivityResult(ActivityResultContracts.GetContent()) {

    }

    private val pickPdf = registerForActivityResult(ActivityResultContracts.GetContent()) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClickListeners()
    }

    private fun initClickListeners() {
        btnPickImage.setOnClickListener(this)
        btnPickVideo.setOnClickListener(this)
        btnPickAudio.setOnClickListener(this)
        btnPickDocument.setOnClickListener(this)
        btnAskForCameraPermission.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnPickImage -> {
                pickImage()
                //requestPermission.launch(android.Manifest.permission.CAMERA)
            }
            R.id.btnPickVideo -> {
                pickVideo()
                //pickVideo.launch("video/*")
            }
            R.id.btnPickAudio -> {
                pickAudio()
                //pickAudio.launch("audio/*")
            }
            R.id.btnPickDocument -> {
                // TODO : Pick Document
                //pickPdf.launch("application/pdf")
            }
            R.id.btnAskForCameraPermission -> {
                //requestPermission.launch(android.Manifest.permission.CAMERA)
                /*requestMultiplePermission.launch(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )*/
                //val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
                //val storageDir = getExternalFilesDir("image/*")

                /*val file =  File.createTempFile(
                    "JPEG_${timeStamp}_",
                    ".jpg",
                    storageDir
                ).apply {
                    currentImagePath = absolutePath
                }
                takePicture.launch()*/
            }
        }
    }

    private fun pickImage() {
        val imageIntent = Intent(this@MainActivity, FilePickerActivity::class.java)
        imageIntent.putExtra(FILE_TYPE, FileType.IMAGE.name)
        startActivityForResult(imageIntent, FILE_PICKER_REQUEST_CODE)
    }

    private fun pickVideo() {
        val videoIntent = Intent(this@MainActivity, FilePickerActivity::class.java)
        videoIntent.putExtra(FILE_TYPE, FileType.VIDEO.name)
        startActivityForResult(videoIntent, FILE_PICKER_REQUEST_CODE)
    }

    private fun pickAudio() {
        val videoIntent = Intent(this@MainActivity, FilePickerActivity::class.java)
        videoIntent.putExtra(FILE_TYPE, FileType.AUDIO.name)
        startActivityForResult(videoIntent, FILE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                if (data.hasExtra("FILE")) {
                    Toast.makeText(this, data.extras?.get("FILE").toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}