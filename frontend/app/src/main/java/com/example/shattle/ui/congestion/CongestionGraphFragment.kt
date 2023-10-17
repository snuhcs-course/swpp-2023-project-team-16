package com.example.shattle.ui.congestion

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.example.shattle.R
import com.example.shattle.databinding.FragmentCongestionGraphBinding

class CongestionGraphFragment : DialogFragment() {

    private var _binding: FragmentCongestionGraphBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCongestionGraphBinding.inflate(layoutInflater, container, false)


        binding.closeImageButton.setOnClickListener{
            dismiss()
        }

        return binding.root
    }

    // 뒤로가기 버튼 누르면 닫기
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}