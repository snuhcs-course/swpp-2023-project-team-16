package com.example.shattle.ui.circular

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
import com.example.shattle.R
import com.example.shattle.databinding.FragmentCircularBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CircularFragment : Fragment() {

    private var _binding: FragmentCircularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    private val circularData = CircularData(true)
    private var currentBusLocations = circularData.currentBusLocations
    private var busMarkers: MutableList<Marker> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCircularBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // mapFragment 는 onCreateView 안에서만 초기화하기!!!
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap
            customizeGoogleMap()
            refreshData()
            showCurrentLocationsOfBus()
            updateUpdatedDateTime()
            refreshView()
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun customizeGoogleMap() {

        // set initial camera location
        val initialLocation = LatLng(37.45800, 126.9531)
        val zoomLevel = 14.70f
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, zoomLevel))

        // Show the bus stop locations on the map
        showBusStopLocations()

        // Show the route of the circular shuttle on the map
        showRoute()
    }

    private fun showBusStopLocations() {
        // Show the bus stop locations on the map

        // 기본 마커로도 할 수 있고, 마커 이미지 커스텀 가능 (20~50 픽셀)
        val customMarkerIcon =
            BitmapDescriptorFactory.fromResource(R.drawable.img_circular_bus_stop)

        for (busStop in circularData.busStops) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(busStop.position)
                    .title(busStop.title) // Set a title for the marker
                    .snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
            )
        }
    }

    private fun showRoute() {
        // Show the route of the circular shuttle on the map

        googleMap?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(circularData.roadCoordinates)
                .width(10f) // Set the width of the line
                .color(Color.BLACK) // Set the color of the line
        )
    }

    private fun showCurrentLocationsOfBus() {

        // Remove previous bus markers
        if (busMarkers.size != 0) {
            for (busMarker in busMarkers) {
                busMarker.remove()
            }
            busMarkers.clear()
        }

        if (currentBusLocations == null || currentBusLocations.size == 0) {
            Toast.makeText(activity, R.string.toast_refresh_error, Toast.LENGTH_SHORT).show()
        } else {
            // Add bus markers on the map ("currentBusLocations" holds the locations)
            for (circularBus in currentBusLocations) {
                val busMarker = googleMap?.addMarker(
                    MarkerOptions()
                        .position(circularBus.location) // Set the bus's initial position
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_circular_bus)) // Use a custom bus icon
                    // 20~50픽셀
                )
                if (busMarker != null) {
                    busMarkers.add(busMarker)
                }
            }
        }
    }

    fun updateUpdatedDateTime() {
        var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = inputFormat.parse(circularData.dateTimeString)

        val outputFormat = SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
        binding.updatedTimeTextView.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
    }

    private fun refreshView() {

        // 1. Refresh manually
        binding.refreshButton.setOnClickListener {
            refreshData()
            showCurrentLocationsOfBus()
            updateUpdatedDateTime()
        }

        // 2. Refresh automatically
        // Schedule marker removal after 10 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                refreshData()
                showCurrentLocationsOfBus()
                updateUpdatedDateTime()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MyLogChecker", "error: $e")
            }

        }, 5000) // TODO: change to 10000

    }
    private fun refreshData() {
        circularData.refreshCurrentBusLocation()
        currentBusLocations = circularData.currentBusLocations

    }

}
