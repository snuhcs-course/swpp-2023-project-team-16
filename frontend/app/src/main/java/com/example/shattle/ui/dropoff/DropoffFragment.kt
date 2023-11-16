package com.example.shattle.ui.dropoff

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.databinding.FragmentDropoffBinding

class DropoffFragment : Fragment() {

    private var _binding: FragmentDropoffBinding? = null

    lateinit var dropoffViewModel: DropoffViewModel
    lateinit var dropoffUI: DropoffUI
    lateinit var currentLineUseCase: CurrentLineUseCase

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var refreshRunnable: Runnable

    val binding get() = _binding!!
    var toast: Toast? = null

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
        dropoffViewModel = ViewModelProvider(this).get(DropoffViewModel::class.java)

        // UI elements
        dropoffUI = DropoffUI(
            binding.numPeopleTextView,
            binding.numTimeTextView,
            binding.numBusTextView,
            binding.updatedTimeTextView,
            binding.manImageView,
            binding.visualViewLayout,
            binding.refreshButton,
            binding.busImageView1,
            binding.busImageView2,
            binding.busImageView3,
            binding.busImageView4,
        )

        // init refresh
        dropoffViewModel.getData(currentLineUseCase)

        // ViewModel tracks data changes
        dropoffViewModel.getUIState().observe(viewLifecycleOwner) { newDropoffUIState ->
            dropoffUI.updateUI(newDropoffUIState!!)
        }

        // call 호출이 끝난 경우에만 uiState 의 데이터 및 화면 업데이트
        dropoffViewModel.getNetworkRequestStatus().observe(viewLifecycleOwner) { isFinished ->
            if (isFinished == true) {
                dropoffViewModel.getData(currentLineUseCase)
            }
        }

        // Refresh Button
        dropoffUI.bt_refresh.setOnClickListener {
            dropoffViewModel.notifyRefresh(currentLineUseCase)
            //dropoffViewModel.getData(currentLineUseCase)
        }

        // Toast Message
        dropoffViewModel.getToastMessage().observe(viewLifecycleOwner, Observer { message ->
            if (!message.isNullOrEmpty()) {
                toast?.cancel()
                toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).apply {
                    show()
                }
                dropoffViewModel.showToastMessage("") // Toast 를 띄운 후 메시지 초기화
            }
        })

        // Automatic Refresh (delay: 30 sec)
        refreshRunnable = object : Runnable {
            override fun run() {
                try {
                    dropoffViewModel.notifyRefresh(currentLineUseCase)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("MyLogChecker", "error: $e")
                    // Toast 띄워야하나?
                }
                handler.postDelayed(this, 30000)
            }
        }
        handler.postDelayed(refreshRunnable, 0)
        // 화면 전환 직후 새로고침, 이후 30초마다 새로고침

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toast?.cancel()
        handler.removeCallbacks(refreshRunnable)
        dropoffUI.stopAnimation()
        _binding = null
    }

}