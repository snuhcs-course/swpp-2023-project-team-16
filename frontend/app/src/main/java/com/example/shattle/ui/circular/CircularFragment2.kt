package com.example.shattle.ui.circular

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.R
import com.example.shattle.databinding.FragmentCircular2Binding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class CircularFragment2 : Fragment() {

    private var _binding: FragmentCircular2Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val circularViewModel =
            ViewModelProvider(this).get(CircularViewModel::class.java)

        _binding = FragmentCircular2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        showCurrentLocationOfBus()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCurrentLocationOfBus(){

    }

}