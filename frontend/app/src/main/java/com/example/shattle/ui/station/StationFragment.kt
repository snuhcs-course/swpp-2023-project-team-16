package com.example.shattle.ui.station

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shattle.databinding.FragmentStationBinding


class StationFragment : Fragment() {


    private var _binding: FragmentStationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val stationData = StationData(true)
    private var numberOfPeopleWaitingLine: Int? = null
    private var numberOfNeededBus: Int? = null
    private var waitingTimeInMin: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        refreshData()
        showWaitingData()
        refreshView()

        return root
    }

    private fun refreshData() {
        stationData.refreshWaitingTimeData2()

        numberOfPeopleWaitingLine = stationData.numberOfPeopleWaitingLine
        numberOfNeededBus = stationData.numberOfNeededBus
        waitingTimeInMin = stationData.waitingTimeInMin
    }

    private fun showWaitingData() {

        if (numberOfPeopleWaitingLine == null || numberOfNeededBus == null || waitingTimeInMin == null) {
            Toast.makeText(activity, "정보를 받아오는 중 에러가 발생했습니다", Toast.LENGTH_SHORT).show()
            binding.numPeople.text = "현재 # 명 대기 중입니다."
            binding.numOthers.text =
                "예상 대기시간: # 분\n탈 수 있는 버스: # 번째 버스"
        } else {
            changePeopleImage(numberOfNeededBus!!, numberOfPeopleWaitingLine!!)
            binding.numPeople.text = "현재 $numberOfPeopleWaitingLine 명 대기 중입니다."
            binding.numOthers.text =
                "예상 대기시간: $waitingTimeInMin 분\n탈 수 있는 버스: $numberOfNeededBus 번째 버스"
        }
    }

    private fun changePeopleImage(buses: Int, numPeople: Int) {
        val imageViews = listOf(binding.imageView1, binding.imageView2, binding.imageView3)
        imageViews.forEachIndexed { index, imageView ->
            imageView.setColorFilter(if (index < numPeople && index < buses) Color.BLACK else Color.GRAY)
        }
    }

    private fun refreshView() {

        // 1. Refresh manually
        binding.refreshButton.setOnClickListener {
            refreshData()
            showWaitingData()
        }

        // 2. Refresh automatically
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                refreshData()
                showWaitingData()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MyLogChecker", "error: $e")
            }
        }, 5000) // TODO: change to 10000
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val supportFragmentManager = requireActivity().supportFragmentManager
        val existingFragment = supportFragmentManager.findFragmentByTag("CongestionGraphFragment")
        if (existingFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(existingFragment)
            transaction.commit()
        }
        _binding = null
    }

}