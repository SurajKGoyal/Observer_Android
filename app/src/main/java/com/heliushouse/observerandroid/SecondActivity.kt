package com.heliushouse.observerandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {
    private lateinit var downloadStatus: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        downloadStatus = findViewById(R.id.download_status_view)
        downloadStatus()
    }

    private fun downloadStatus(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                Downloads.downloadStatus.collect {
                    downloadStatus.text = "${it.downloadPercentage ?: 0}"
                }
            }

        }

    }
}