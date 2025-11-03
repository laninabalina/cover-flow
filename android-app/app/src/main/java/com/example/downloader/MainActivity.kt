package com.example.downloader

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.downloader.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestWritePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startDownload()
        } else {
            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.downloadButton.setOnClickListener {
            if (needsLegacyWritePermission() && !hasLegacyWritePermission()) {
                requestWritePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                startDownload()
            }
        }
    }

    private fun startDownload() {
        val url = binding.urlInput.text?.toString()?.trim()
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "Enter a URL", Toast.LENGTH_SHORT).show()
            return
        }
        val uri = try {
            Uri.parse(url)
        } catch (_: Exception) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = guessFileName(uri.toString())
        val request = DownloadManager.Request(uri)
            .setTitle(fileName)
            .setDescription("Downloading $fileName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        // Destination in public Downloads. For API 29+, DownloadManager handles permissions.
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        try {
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to start download: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun guessFileName(url: String): String {
        val path = Uri.parse(url).lastPathSegment ?: "download"
        var name = path.substringAfterLast('/').ifEmpty { "download" }
        if (!name.contains(".")) {
            // Try to infer extension from URL parameters
            name += ".bin"
        }
        // Sanitize
        name = name.replace(Regex("[^A-Za-z0-9._-]"), "_")
        return name.lowercase(Locale.US)
    }

    private fun needsLegacyWritePermission(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    private fun hasLegacyWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}