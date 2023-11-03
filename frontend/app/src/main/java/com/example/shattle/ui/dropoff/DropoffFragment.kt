package com.example.shattle.ui.dropoff

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.databinding.FragmentDropoffBinding

class DropoffFragment : Fragment() {

    var dateTimeString: String? = null
    var isFirstStart = true

    private var _binding: FragmentDropoffBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDropoffBinding.inflate(inflater, container, false)

        // Data Source
        val currentLineDataSource = CurrentLineDataSource(requireContext())

        // Repository
        val currentLineRepository = CurrentLineRepository(currentLineDataSource)

        // Utils
        // Use Case
        val currentLineUseCase = CurrentLineUseCase(currentLineRepository)

        // View Model
        val dropoffViewModel = ViewModelProvider(this).get(DropoffViewModel::class.java)

        // UI elements
        val dropoffUI = DropoffUI(
            binding.numPeopleTextView,
            binding.numBusTextView,
            binding.numTimeTextView,
            binding.updatedTimeTextView,
            binding.manImageView,
            binding.visualViewLayout,
            binding.refreshButton
        )

        // ViewModel tracks data changes
        dropoffViewModel.getUIState().observe(viewLifecycleOwner) { newDropoffUIState ->
            dropoffUI.updateUI(newDropoffUIState!!)
        }

        // Initial Update
        dropoffViewModel.notifyRefresh(currentLineUseCase)
        dropoffViewModel.getData(currentLineUseCase)

        // Refresh Button
        dropoffUI.bt_refreshButton.setOnClickListener{
            dropoffViewModel.notifyRefresh(currentLineUseCase)
            dropoffViewModel.getData(currentLineUseCase)
        }

        dropoffViewModel.getToastMessage().observe(viewLifecycleOwner, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                dropoffViewModel.showToastMessage("") // Toast를 띄운 후 메시지 초기화
            }
        })

        // Automatic Refresh (30 sec)
        val handler = Handler(Looper.getMainLooper())
        val refreshRunnable = object : Runnable {
            override fun run() {
                try {
                    dropoffViewModel.notifyRefresh(currentLineUseCase)
                    dropoffViewModel.getData(currentLineUseCase)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("MyLogChecker", "error: $e")
                }
                handler.postDelayed(this, 30000)
            }
        }
        handler.postDelayed(refreshRunnable, 30000)

        val root: View = binding.root
        return root
    }

//    fun changeView() {
//
//        // 서버 호출이 성공한 경우에만 화면 업데이트, 호출 실패 시 toast 만 띄워주고 화면 업데이트 X
//        if (dropoffViewModel.isSuccessCall) {
//            changeVisualView()
//            changeTextView()
//            updateUpdatedDateTime()
//        } else {
//            Toast.makeText(activity, R.string.toast_refresh_error, Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}