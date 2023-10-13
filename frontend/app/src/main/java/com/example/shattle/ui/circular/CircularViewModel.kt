package com.example.shattle.ui.circular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CircularViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is circular Fragment"
    }
    val text: LiveData<String> = _text
}