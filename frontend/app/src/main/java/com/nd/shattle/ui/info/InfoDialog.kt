package com.nd.shattle.ui.info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.nd.shattle.databinding.DialogInfoBinding

class InfoDialog : DialogFragment() {

    private var _binding: DialogInfoBinding? = null
    private val binding get() = _binding!!

    private var listener: InfoDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // MainActivity 에서 Dialog 가 dismiss 됐는지 여부를 전달하기 위한 listener
        listener = context as? InfoDialogListener
        if (listener == null) {
            throw ClassCastException("$context must implement InfoDialogListener")
        }

        // 뒤로가기 버튼 클릭 시 dismiss
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dismiss()
                listener?.onDialogDismissed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogInfoBinding.inflate(layoutInflater, container, false)

        isCancelable = true

        binding.closeImageButton.setOnClickListener{
            dismiss()
            listener?.onDialogDismissed()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // DialogFragment와 Activity 간의 상호작용을 위한 인터페이스
    interface InfoDialogListener {
        fun onDialogDismissed()
    }
}