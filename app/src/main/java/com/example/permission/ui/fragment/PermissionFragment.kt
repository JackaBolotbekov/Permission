package com.example.permission.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.example.permission.BuildConfig
import com.example.permission.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {

    private lateinit var binding: FragmentPermissionBinding

    private val permissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (!result) {
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse
                                ("package:" + BuildConfig.APPLICATION_ID)))
                }
            } else {
                takePicturesIntent.launch("image/*")
            }
        }

    private val takePicturesIntent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.image.load(it)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            initialize()
        }
    }

    private fun initialize() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    != PackageManager.PERMISSION_GRANTED -> {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                takePicturesIntent.launch("image/*")

            }
        }
    }
}