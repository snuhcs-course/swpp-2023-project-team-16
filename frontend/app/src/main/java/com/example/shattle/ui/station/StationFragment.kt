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
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.shattle.R
import com.example.shattle.databinding.FragmentStationBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


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
        changeView()
        refreshView()
        updateUpdatedDateTime()

        return root
    }

    private fun refreshData() {
        stationData.refreshWaitingTimeData()

        numberOfPeopleWaitingLine = stationData.numberOfPeopleWaitingLine
        numberOfNeededBus = stationData.numberOfNeededBus
        waitingTimeInMin = stationData.waitingTimeInMin
    }

    fun changeView() {

        if (numberOfPeopleWaitingLine == null || numberOfNeededBus == null || waitingTimeInMin == null) {
            Toast.makeText(activity, R.string.toast_refresh_error, Toast.LENGTH_SHORT).show()
        } else {
            changeVisualView(numberOfPeopleWaitingLine!!, numberOfNeededBus!!)
            changeTextView()
        }
    }

    fun changeVisualView(numPeople: Int, buses: Int) {
        val imageView = binding.manImageView

        val constraintLayout = binding.visualViewLayout

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Change the bias values as desired.
        var bias = (numPeople / 120.0).toFloat()
        if(bias < 0.1)
            bias = 0.15F
        constraintSet.setHorizontalBias(R.id.manImageView, (bias))
        // Apply the updated constraints to the ConstraintLayout.
        constraintSet.applyTo(constraintLayout)

    }

    fun changeTextView() {
        binding.numPeople.text = "현재 ${numberOfPeopleWaitingLine}명 대기 중입니다."
        binding.numBus.text = "다음 ${numberOfNeededBus}번째 버스 탑승 가능합니다."
        binding.numMinute.text = "예상 대기시간: ${waitingTimeInMin}분"
    }

    fun updateUpdatedDateTime() {
        var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = inputFormat.parse(stationData.dateTimeString)

        val outputFormat = SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
        binding.updatedTimeTextView.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
    }

    fun refreshView() {

        // 1. Refresh manually
        binding.refreshButton.setOnClickListener {
            refreshData()
            changeView()
            updateUpdatedDateTime()
        }

        // 2. Refresh automatically
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                refreshData()
                changeView()
                updateUpdatedDateTime()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MyLogChecker", "error: $e")
            }
        }, 5000) // TODO: change to 10000
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}