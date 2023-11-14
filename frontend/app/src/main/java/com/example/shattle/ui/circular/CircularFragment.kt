package com.example.shattle.ui.circular

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
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
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class CircularFragment : Fragment() {

    private var _binding: FragmentCircularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var googleMap: GoogleMap? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var refreshRunnable: Runnable

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

        // call 호출이 끝난 경우에만 uiState 의 데이터 및 화면 업데이트
        circularViewModel.getNetworkRequestStatus().observe(viewLifecycleOwner) { isFinished ->
            if (isFinished == true) {
                circularViewModel.getData(runningBusesUseCase)
            }
        }

        // User Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        circularUI.bt_gps.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                circularViewModel.showToastMessage("사용자의 현재 위치를 확인하려면 위치 권한을 허용해 주세요.")
            } else {
                circularUI.drawUserLocation(
                    googleMap,
                    requireContext(),
                    requireActivity(),
                    fusedLocationClient
                )
            }
        }

        // Refresh Button
        circularUI.bt_refresh.setOnClickListener {
            circularViewModel.notifyRefresh(runningBusesUseCase)
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
        refreshRunnable = object : Runnable {
            override fun run() {
                try {
                    circularViewModel.notifyRefresh(runningBusesUseCase)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("MyLogChecker", "error on refreshRunnable.run(): \n\t $e")
                }
                handler.postDelayed(this, 30000)
            }
        }
        handler.postDelayed(refreshRunnable, 0)
        // 화면 전환 직후 새로고침, 이후 30초마다 자동 새로고침

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
