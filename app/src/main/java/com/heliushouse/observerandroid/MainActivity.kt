package com.heliushouse.observerandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var downloadStatusView: TextView
    private lateinit var nextActivityButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        downloadStatusView = findViewById(R.id.download_status_text)
        nextActivityButton = findViewById(R.id.next_activity)
        nextActivityButton.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        startService(Intent(this, DownloadService::class.java))
        downloadStatus()
    }

    private fun downloadStatus(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                Downloads.downloadStatus.collect {
                    downloadStatusView.text = "${it.downloadPercentage ?: 0}"
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, DownloadService::class.java))
    }
}