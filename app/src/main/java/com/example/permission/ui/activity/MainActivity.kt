package com.example.permission.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import coil.load
import com.example.permission.BuildConfig
import com.example.permission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val permissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (!result) {
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse
                                ("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
            } else {
                takePicturesIntent.launch("image/*")
            }
        }

    private val takePicturesIntent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.image.load(it)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            initialize()
        }
    }

    private fun initialize() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED -> {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                takePicturesIntent.launch("image/*")

            }
        }
    }
}