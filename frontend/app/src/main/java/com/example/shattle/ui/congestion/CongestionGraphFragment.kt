package com.example.shattle.ui.congestion_graph

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.shattle.databinding.FragmentCongestionGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CongestionGraphFragment : DialogFragment() {

    private var _binding: FragmentCongestionGraphBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCongestionGraphBinding.inflate(inflater, container, false)
        val view = binding.root

        setupChart(binding.dayTimeChart)
        loadGraphData(binding.dayTimeChart)

        binding.closeImageButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.hide(this)?.commit()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupChart(chart: LineChart) {
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = TimeValueFormatter()
    }

    private fun loadGraphData(chart: LineChart) {
        val congestionData = generateDummyData()
        val entries = congestionData.mapIndexed { index, value ->
            Entry(index.toFloat(), value)
        }

        val dataSet = LineDataSet(entries, "Congestion")
        dataSet.color = Color.BLACK
        dataSet.setCircleColor(Color.BLACK)

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    private fun generateDummyData(): List<Float> {
        // Replace this with your actual data loading logic
        val congestionData = mutableListOf<Float>()
        // Add your congestion values here
        return congestionData
    }

    inner class TimeValueFormatter : ValueFormatter() {
        private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val millis = value.toLong() * 60000
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millis
            return sdf.format(calendar.time)
        }
    }
}