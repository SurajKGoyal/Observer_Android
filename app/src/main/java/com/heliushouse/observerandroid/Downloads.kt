package com.heliushouse.observerandroid

import com.heliushouse.observerandroid.model.DownloadableItem
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

object Downloads {
    val downloadStatus = MutableSharedFlow<DownloadableItem>()
}