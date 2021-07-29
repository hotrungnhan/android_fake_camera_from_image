package com.example.fakecameraimage

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : ComponentActivity() {
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        try {
            copyFile(
                it,
                intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT)
            )
            this.setResult(RESULT_OK)
        } catch (e: Exception) {
            this.setResult(RESULT_CANCELED)
        }
        finish()
    }

    private fun copyFile(src: Uri?, dst: Uri?) {
        if (src != null && dst != null) {
            val inChannel = contentResolver.openInputStream(src)
            val outChannel = contentResolver.openOutputStream(dst)

            try {
                outChannel?.write(inChannel?.readBytes())

            } finally {
                inChannel?.close()
                outChannel?.close()
            }
        } else {
            throw NullPointerException("src and dst cant be null: $src \n $dst")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent.action) {
            MediaStore.ACTION_IMAGE_CAPTURE -> getContent.launch("image/*")
            MediaStore.ACTION_VIDEO_CAPTURE -> getContent.launch("video/*")
            else -> {
                this.setResult(RESULT_CANCELED)
                finish()
            }
        }
    }
}