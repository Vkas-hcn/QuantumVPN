package com.bee.open.ant.fast.composeopen.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null
    private var secondsElapsed = 0

    private val _timerText = MutableStateFlow("00:00:00")
    val timerText: StateFlow<String> = _timerText

    fun startTimer() {
        timerJob?.cancel()
        secondsElapsed = 0
        timerJob = viewModelScope.launch {
            while (true) {
                secondsElapsed++
                _timerText.value = formatTime(secondsElapsed)
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        secondsElapsed = 0
        _timerText.value = formatTime(secondsElapsed)
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }
}
