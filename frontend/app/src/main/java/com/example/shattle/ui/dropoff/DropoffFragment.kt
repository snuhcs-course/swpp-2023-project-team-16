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

    private var _binding: FragmentDropoffBinding? = null

    lateinit var dropoffViewModel: DropoffViewModel
    lateinit var currentLineUseCase: CurrentLineUseCase

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var refreshRunnable: Runnable

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val dropoffViewModel = ViewModelProvider(this).get(DropoffViewModel::class.java)

        // UI elements
        val dropoffUI = DropoffUI(
            binding.numPeopleTextView,
            binding.numBusTextView,
            binding.numTimeTextView,
            binding.updatedTimeTextView,
            binding.manImageView,
            binding.visualViewLayout,
            binding.refreshButton,
        )

        // Initial update
        dropoffViewModel.notifyRefresh(currentLineUseCase)
        dropoffViewModel.getDataInit(currentLineUseCase)

        // ViewModel tracks data changes
        dropoffViewModel.getUIState().observe(viewLifecycleOwner) { newDropoffUIState ->
            dropoffUI.updateUI(newDropoffUIState!!)
        }

        // Refresh Button
        dropoffUI.bt_refresh.setOnClickListener {
            dropoffViewModel.notifyRefresh(currentLineUseCase)
            dropoffViewModel.getData(currentLineUseCase)
        }

        // Toast Message
        dropoffViewModel.getToastMessage().observe(viewLifecycleOwner, Observer { message ->
            if (!message.isNullOrEmpty()) {
                toast?.cancel()
                toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).apply {
                    show()
                }
                dropoffViewModel.showToastMessage("") // Toast를 띄운 후 메시지 초기화
            }
        })

        // Automatic Refresh (delay: 30 sec)
        refreshRunnable = object : Runnable {
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
        handler.postDelayed(refreshRunnable, 500)
        // 화면 전환 후 0.5초 뒤 새로고침, 이후 30초마다 새로고침

        val root: View = binding.root
        return root
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toast?.cancel()
        handler.removeCallbacks(refreshRunnable)
        _binding = null
    }

}