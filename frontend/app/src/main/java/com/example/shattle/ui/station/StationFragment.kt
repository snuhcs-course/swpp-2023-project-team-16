package com.example.shattle.ui.station

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.MainActivity
import com.example.shattle.R
import com.example.shattle.data.models.ResponseWaitingLine
import com.example.shattle.databinding.FragmentStationBinding
import com.example.shattle.network.ServiceCreator
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationFragment : Fragment() {


    private var _binding: FragmentStationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val handler = Handler()
    private lateinit var dataFetchRunnable: Runnable


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val stationViewModel =
            ViewModelProvider(this).get(StationViewModel::class.java)

        _binding = FragmentStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textStation
//        stationViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        binding.refreshButton.setOnClickListener {
            //getWaitingTimeData()
        }

        // Refresh every 5 seconds.
        dataFetchRunnable = object : Runnable {
            override fun run() {
                // Call your data-fetching function here
                //getWaitingTimeData()

                // Schedule the next execution in 5 seconds
                handler.postDelayed(this, 5000) // 5000 milliseconds = 5 seconds
            }
        }

        // Start the initial execution
        handler.post(dataFetchRunnable)


        return root
    }

    private fun getWaitingTimeData() {
        val call: Call<ResponseWaitingLine> = ServiceCreator.apiService.getWaitingLine()

        call.enqueue(object : Callback<ResponseWaitingLine> {
            override fun onResponse(
                call: Call<ResponseWaitingLine>,
                response: Response<ResponseWaitingLine>
            ) {
                if (response.isSuccessful) {
                    val numberOfPeopleWaitingLine = response.body()?.numberOfPeopleWaiting
                    val numberOfNeededBus = response.body()?.numberOfNeededBuses
                    val waitingTimeInMin = response.body()?.waitingTimeInMin
                    if (numberOfNeededBus == null
                        || numberOfPeopleWaitingLine == null
                        || waitingTimeInMin == null
                    ) {
                        Toast.makeText(activity, "정보를 받아오는 중 에러가 발생했습니다", Toast.LENGTH_SHORT).show()
                        Log.e("Station", "error: response contained null data")
                    } else {
                        changePeopleImage(numberOfNeededBus, numberOfPeopleWaitingLine);
                        var numPeople = binding.numPeople
                        numPeople.setText("현재 $numberOfPeopleWaitingLine 명 대기 중입니다.")
                        var numOthers = binding.numOthers
                        numOthers.setText("예상 대기시간: $waitingTimeInMin 분\n탈 수 있는 버스: $numberOfNeededBus 번쨰 버스")
                    }

                }
            }

            override fun onFailure(call: Call<ResponseWaitingLine>, t: Throwable) {
                Log.e("Station", "error: $t")
            }
        })
    }

    private fun changePeopleImage(buses: Int, numPeople: Int) {
        var im1 = binding.imageView1
        var im2 = binding.imageView2
        var im3 = binding.imageView3
        im1.setColorFilter(Color.GRAY)
        im2.setColorFilter(Color.GRAY)
        im3.setColorFilter(Color.GRAY)
        if (numPeople == 0) return else im1.setColorFilter(Color.BLACK)
        if (buses > 1) im2.setColorFilter(Color.BLACK)
        if (buses > 2) im3.setColorFilter(Color.BLACK)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}