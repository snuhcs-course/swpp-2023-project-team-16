package com.example.shattle.ui.circular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    private val circularData = CircularData(true)


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
            showCurrentLocationOfBuses()
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

        // show the bus stop locations on the map
        showBusStopLocations()

        // show the route of the circular shuttle on the map
        showRoute()
    }

    private fun showBusStopLocations() {

        val customMarkerIcon =
            BitmapDescriptorFactory.fromResource(R.drawable.img_bus_stop_marker)

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

        googleMap?.addPolyline(
            PolylineOptions()
                .addAll(circularData.roadCoordinates)
                .width(10f) // Set the width of the line
            //.color(Color.RED) // Set the color of the line
        )
    }

    private fun showCurrentLocationOfBuses() {
        // TODO
        // Create a marker for a bus
        val busMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(37.46577, 126.9484)) // Set the bus's initial position
                .title("Bus Name") // Set the title for the marker (optional)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_bus)) // Use a custom bus icon
        )

        // Update the bus marker's position when you receive real-time location updates
        binding.refreshButton.setOnClickListener {
            val newBusLocation = LatLng(37.44809, 126.9521) // New bus location coordinates
            busMarker?.position = newBusLocation
        }

    }

}