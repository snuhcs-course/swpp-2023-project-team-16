package com.example.shattle.ui.circular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.R
import com.example.shattle.databinding.FragmentCircularBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class CircularFragment : Fragment() {

    private var _binding: FragmentCircularBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val circularViewModel =
            ViewModelProvider(this).get(CircularViewModel::class.java)

        _binding = FragmentCircularBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap
            customizeGoogleMap()
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

        showMarkers()

        showRoute()

        showCurrentLocationOfBuses(binding)

    }

    private fun showMarkers() {
        val busStops = listOf(
            BusStop(LatLng(37.46577, 126.9484), "정문", ""),
            BusStop(LatLng(37.46306, 126.9490), "법과대", ""),
            BusStop(LatLng(37.46046, 126.9490), "자연대", ""),
            BusStop(LatLng(37.45703, 126.9493), "농생대", ""),
            BusStop(LatLng(37.45502, 126.9498), "38동, 공대입구", ""),
            BusStop(LatLng(37.45356, 126.9502), "신소재", ""),
            BusStop(LatLng(37.44809, 126.9521), "302동", ""),
            BusStop(LatLng(37.45158, 126.9526), "301동, 유회진학술정보관", ""),
            BusStop(LatLng(37.45408, 126.9539), "유전공학연구소", ""),
            BusStop(LatLng(37.45612, 126.9554), "교수회관입구", ""),
            BusStop(LatLng(37.46103, 126.9565), "기숙사삼거리", ""),
            BusStop(LatLng(37.46418, 126.9553), "국제대학원", ""),
            BusStop(LatLng(37.46606, 126.9546), "수의대", ""),
            BusStop(LatLng(37.46602, 126.9522), "경영대", ""),
        )

        val customMarkerIcon =
            BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_location_png)

        for (busStop in busStops) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(busStop.position)
                    .title(busStop.title) // Set a title for the marker
                    .snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
            )
        }
    }

    private fun showCurrentLocationOfBuses(binding: FragmentCircularBinding) {
        // TODO
        // Create a marker for a bus
        val busMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(37.46577, 126.9484)) // Set the bus's initial position
                .title("Bus Name") // Set the title for the marker (optional)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bus_png)) // Use a custom bus icon
        )

        // Update the bus marker's position when you receive real-time location updates
        binding.refreshButton.setOnClickListener{
            val newBusLocation = LatLng(37.45158, 126.9526) // New bus location coordinates
            busMarker?.position = newBusLocation
        }

    }

    private fun showRoute() {

        val roadCoordinates = listOf(
            LatLng(37.46577, 126.9484),
            LatLng(37.44809, 126.9521),
            LatLng(37.46103, 126.9565),
            LatLng(37.46577, 126.9484),
        )

        googleMap?.addPolyline(
            PolylineOptions()
                .addAll(roadCoordinates)
                .width(10f) // Set the width of the line
                //.color(Color.RED) // Set the color of the line
        )
    }

    data class BusStop(val position: LatLng, val title: String, val snippet: String) {

    }

    data class Bus(val position: LatLng) {

    }

}