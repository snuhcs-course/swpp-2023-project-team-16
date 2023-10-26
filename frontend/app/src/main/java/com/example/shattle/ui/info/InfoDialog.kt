package com.example.shattle.ui.info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.example.shattle.databinding.DialogInfoBinding

class InfoDialog : DialogFragment() {

    private var _binding: DialogInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogInfoBinding.inflate(layoutInflater, container, false)


        binding.closeImageButton.setOnClickListener{
            dismiss()
        }

        return binding.root
    }

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