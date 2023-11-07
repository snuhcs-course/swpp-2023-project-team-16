package com.example.shattle.ui.circular

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
import com.example.shattle.R
import com.example.shattle.databinding.FragmentCircularBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class CircularFragment : Fragment() {

    private var _binding: FragmentCircularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCircularBinding.inflate(inflater, container, false)

        // Data Source
        val runningBusesDataSource = RunningBusesDataSource(requireContext())

        // Repository
        val runningBusesRepository = RunningBusesRepository(runningBusesDataSource)

        // Utils

        // Use Case
        val runningBusesUseCase = RunningBusesUseCase(runningBusesRepository)

        // View Model
        val circularViewModel = ViewModelProvider(this).get(CircularViewModel::class.java)

        // UI elements
        val circularUI = CircularUI(
            binding.refreshButton,
            binding.updatedTimeTextView
        )

        // ViewModel tracks data changes
        circularViewModel.getUIState().observe(viewLifecycleOwner) { newCircularUIState ->
            circularUI.updateUI(googleMap, newCircularUIState!!)
        }


        // Set Google Map async
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment // mapFragment 는 onCreateView 안에서만 초기화하기!!!
        mapFragment.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap
            //circularViewModel.setGoogleMap(googleMap!!)
            circularUI.customizeGoogleMap(googleMap!!)
            circularUI.showCurrentLocationsOfBus(googleMap!!, circularViewModel.getUIState().value!!)
        })

        // Initial Update
        circularViewModel.notifyRefresh(runningBusesUseCase)
        circularViewModel.getData(runningBusesUseCase)

        // Refresh Button
        circularUI.bt_refreshButton.setOnClickListener {
            circularViewModel.notifyRefresh(runningBusesUseCase)
            circularViewModel.getData(runningBusesUseCase)
        }

        // Toast Message
        circularViewModel.getToastMessage().observe(viewLifecycleOwner, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                circularViewModel.showToastMessage("") // Toast를 띄운 후 메시지 초기화
            }
        })

        // Automatic Refresh (delay: 30 sec)
        val handler = Handler(Looper.getMainLooper())
        val refreshRunnable = object : Runnable {
            override fun run() {
                try {
                    circularViewModel.notifyRefresh(runningBusesUseCase)
                    circularViewModel.getData(runningBusesUseCase)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
