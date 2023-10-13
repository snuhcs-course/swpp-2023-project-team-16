package com.example.shattle.ui.lostnfound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LostnfoundViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lost & found Fragment"
    }
    val text: LiveData<String> = _text
}