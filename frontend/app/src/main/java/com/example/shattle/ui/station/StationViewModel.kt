package com.example.shattle.ui.station

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is station Fragment"
    }
    val text: LiveData<String> = _text
}