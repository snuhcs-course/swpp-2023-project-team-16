package com.example.shattle.ui.station

import android.os.Bundle
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
        //getWaitingTimeData();
        return root
    }

    private fun getWaitingTimeData() {
        val call: Call<ResponseWaitingLine> = ServiceCreator.apiService.getWaitingLine()

        call.enqueue(object: Callback<ResponseWaitingLine> {
            override fun onResponse(
                call: Call<ResponseWaitingLine>,
                response: Response<ResponseWaitingLine>
            ) {
                if (response.isSuccessful) {
                    val numberOfPeopleWaitingLine = response.body()?.numberOfPeopleWaiting
                    val numberOfNeededBus = response.body()?.numberOfNeededBuses
                    val waitingTimeInMin = response.body()?.waitingTimeInMin
                    //TODO
                }
            }

            override fun onFailure(call: Call<ResponseWaitingLine>, t: Throwable) {
                Log.e("Station", "error: $t")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}