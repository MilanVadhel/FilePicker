package com.wayfortech.filepickerdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.wayfortech.filepickerdemo.enum.FileType
import com.wayfortech.filepickerdemo.utils.Constants.FILE_TYPE
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClickListeners()
    }

    private fun initClickListeners() {
        btnPickImage.setOnClickListener(this)
        btnPickVideo.setOnClickListener(this)
        btnPickAudio.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnPickImage -> {
                pickContent(FileType.IMAGE)
            }
            R.id.btnPickVideo -> {
                pickContent(FileType.VIDEO)
            }
            R.id.btnPickAudio -> {
                pickContent(FileType.AUDIO)
            }
        }
    }

    private fun pickContent(fileType: FileType) {
        val intent = Intent(this@MainActivity, FilePickerActivity::class.java)
        when (fileType) {
            FileType.IMAGE -> {
                intent.putExtra(FILE_TYPE, FileType.IMAGE.name)
                resultLauncher.launch(intent)
            }
            FileType.VIDEO -> {
                intent.putExtra(FILE_TYPE, FileType.VIDEO.name)
                resultLauncher.launch(intent)
            }
            FileType.AUDIO -> {
                intent.putExtra(FILE_TYPE, FileType.AUDIO.name)
                resultLauncher.launch(intent)
            }
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    val uri = it.extras?.get("FILE")
                    Toast.makeText(this, "$uri", Toast.LENGTH_SHORT).show()
                    //TODO : Do whatever you want with your Uri
                }
            }
        }
}