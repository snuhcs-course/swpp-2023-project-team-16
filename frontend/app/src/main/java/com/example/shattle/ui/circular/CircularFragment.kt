package com.example.shattle.ui.circular

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.R
import com.example.shattle.databinding.FragmentCircularBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CircularFragment : Fragment() {

    private var _binding: FragmentCircularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var googleMap: GoogleMap? = null
    private lateinit var circularUI: CircularUI
    private lateinit var circularViewModel: CircularViewModel
    var toast: Toast? = null

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
        circularViewModel = ViewModelProvider(this).get(CircularViewModel::class.java)

        // UI elements
        circularUI = CircularUI(
            binding.refreshButton,
            binding.updatedTimeTextView,
            binding.gpsImageButton,
        )

        // ViewModel tracks data changes
        circularViewModel.getUIState().observe(viewLifecycleOwner) { newCircularUIState ->
            circularUI.updateUI(googleMap, newCircularUIState!!, requireContext())
        }

        // Set Google Map async
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment // mapFragment 는 onCreateView 안에서만 초기화하기!!!
        mapFragment.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            //circularViewModel.setGoogleMap(googleMap!!)
            circularUI.customizeGoogleMap(googleMap, requireContext())
            circularUI.drawCurrentLocationsOfBus(
                googleMap,
                circularViewModel.getUIState().value!!,
                requireContext()
            )
        })

        // Initial Update
        circularViewModel.notifyRefresh(runningBusesUseCase)
        circularViewModel.getData(runningBusesUseCase)

        // GPS Button
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        circularUI.bt_gps.setOnClickListener {
            circularUI.drawUserLocation(
                googleMap,
                requireContext(),
                requireActivity(),
                fusedLocationClient,
                LOCATION_PERMISSION_REQUEST_CODE
            )
            if (!circularUI.isUserLocationInBound()) {
                circularViewModel.showToastMessage("현재 학교 밖입니다.")
            }

        }

        // Refresh Button
        circularUI.bt_refresh.setOnClickListener {
            circularViewModel.notifyRefresh(runningBusesUseCase)
            circularViewModel.getData(runningBusesUseCase)
        }

        // Toast Message
        circularViewModel.getToastMessage().observe(viewLifecycleOwner, Observer { message ->
            if (!message.isNullOrEmpty()) {
                toast?.cancel()
                toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).apply {
                    show()
                }
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
                    Log.e("MyLogChecker", "error on refreshRunnable.run(): \n\t $e")
                }
                handler.postDelayed(this, 30000)
            }
        }
        handler.postDelayed(refreshRunnable, 30000)

        val root: View = binding.root
        return root
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var LOCATION_PERMISSION_REQUEST_CODE = 1000
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한이 승인되면 현재 위치를 가져옵니다.
                    circularUI.drawUserLocation(
                        googleMap,
                        requireContext(),
                        requireActivity(),
                        fusedLocationClient,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                    if (!circularUI.isUserLocationInBound()) {
                        circularViewModel.showToastMessage("현재 학교 밖입니다.")
                    }
                } else {
                    // 권한이 거부되면 처리 로직을 여기에 추가...
                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toast?.cancel()
        _binding = null
    }

}
