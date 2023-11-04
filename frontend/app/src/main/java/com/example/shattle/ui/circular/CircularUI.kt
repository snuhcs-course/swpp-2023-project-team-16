package com.example.shattle.ui.circular

import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.shattle.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class CircularUI(
    val bt_refreshButton: Button,
    val tv_updatedTime: TextView
) {
    var busMarkers: MutableList<Marker> = mutableListOf()

    fun updateUI(googleMap: GoogleMap?, circularUIState: CircularUIState) {

        showCurrentLocationsOfBus(googleMap, circularUIState)
        changeUpdatedDateTime(circularUIState)
    }

    fun showCurrentLocationsOfBus(googleMap: GoogleMap?, circularUIState: CircularUIState) {

        // Remove previous bus markers
        if (busMarkers.size != 0) {
            for (busMarker in busMarkers) {
                busMarker.remove()
            }
            busMarkers.clear()
        }

        val buses = circularUIState.buses

        // Add bus markers on the map ("currentBusLocations" holds the locations)
        for (bus in buses) {
            val location = LatLng(bus.latitude, bus.longitude)
            val busMarker = googleMap?.addMarker(
                MarkerOptions()
                    .position(location) // Set the bus's initial position
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_circular_bus)) // Use a custom bus icon
                // 20~50픽셀
            )
            if (busMarker != null) {
                busMarkers.add(busMarker)
            }
        }

    }

    fun changeUpdatedDateTime(circularUIState: CircularUIState) {
        // TODO
        var dateTimeString = "2023-01-23T12:34:56Z"
        tv_updatedTime
//        var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
//        val dateTime = inputFormat.parse(circularData.dateTimeString)
//
//        val outputFormat = SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
//        binding.updatedTimeTextView.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
    }

    fun customizeGoogleMap(googleMap: GoogleMap) {

        // set initial camera location
        val initialLocation = LatLng(37.45800, 126.9531)
        val zoomLevel = 14.70f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, zoomLevel))

        // Show the bus stop locations on the map
        showBusStopLocations(googleMap)

        // Show the route of the circular shuttle on the map
        showRoute(googleMap)
    }

    data class BusStop(val location: LatLng, val title:String, val snippet:String)
    val busStops = listOf(
        BusStop(LatLng(37.46577, 126.9484), "정문", ""),
        BusStop(LatLng(37.46306, 126.9490), "법대, 사회대입구", ""),
        BusStop(LatLng(37.46046, 126.9490), "자연대, 행정관입구", ""),
        BusStop(LatLng(37.45703, 126.9493), "농생대", ""),
        BusStop(LatLng(37.45502, 126.9498), "38동, 공대입구", ""),
        BusStop(LatLng(37.45356, 126.9502), "신소재", ""),
        BusStop(LatLng(37.44809, 126.9521), "제2공학관", ""),
        BusStop(LatLng(37.45158, 126.9526), "301동, 유회진학술정보관", ""),
        BusStop(LatLng(37.45408, 126.9539), "유전공학연구소", ""),
        BusStop(LatLng(37.45612, 126.9554), "교수회관입구", ""),
        BusStop(LatLng(37.46103, 126.9565), "기숙사삼거리", ""),
        BusStop(LatLng(37.46418, 126.9553), "국제대학원", ""),
        BusStop(LatLng(37.46606, 126.9546), "수의대", ""),
        BusStop(LatLng(37.46602, 126.9522), "경영대", ""),
    )

    fun showBusStopLocations(googleMap: GoogleMap) {
        // Show the bus stop locations on the map

        // 기본 마커로도 할 수 있고, 마커 이미지 커스텀 가능 (20~50 픽셀)
        val customMarkerIcon =
            BitmapDescriptorFactory.fromResource(R.drawable.img_circular_bus_stop)

        for (busStop in busStops) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(busStop.location)
                    .title(busStop.title) // Set a title for the marker
                    .snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
            )
        }
    }

    val roadCoordinates = listOf(
        LatLng(37.46577, 126.9484), //정문
        LatLng(37.46306, 126.9490), //법대, 사회대입구
        LatLng(37.461501, 126.949363),
        LatLng(37.46046, 126.9490), //자연대, 행정관입구
        LatLng(37.459559, 126.948701),
        LatLng(37.45703, 126.9493), //농생대
        LatLng(37.45502, 126.9498), //38동, 공대입구
        LatLng(37.45356, 126.9502), //신소재
        LatLng(37.452703, 126.950310),
        LatLng(37.452005, 126.949935),
        LatLng(37.450779, 126.949784),
        LatLng(37.449544, 126.949784),
        LatLng(37.448044, 126.949162),
        LatLng(37.447695, 126.949216),
        LatLng(37.447312, 126.949505),
        LatLng(37.447056, 126.951190),
        LatLng(37.447389, 126.951823),
        LatLng(37.447789, 126.952048),
        LatLng(37.44809, 126.9521), //제2공학관
        LatLng(37.450446, 126.951968),
        LatLng(37.45158, 126.9526), //301동, 유회진학술정보관
        LatLng(37.45408, 126.9539), //유전공학연구소
        LatLng(37.45612, 126.9554), //교수회관입구
        LatLng(37.457299, 126.956277),
        LatLng(37.458582, 126.956641),
        LatLng(37.459546, 126.957172),
        LatLng(37.46103, 126.9565), //기숙사삼거리
        LatLng(37.461288, 126.956138),
        LatLng(37.461556, 126.954807),
        LatLng(37.461862, 126.954544),
        LatLng(37.463199, 126.954695),
        LatLng(37.46418, 126.9553), //국제대학원
        LatLng(37.465545, 126.955139),
        LatLng(37.465784, 126.954967),
        LatLng(37.46606, 126.9546), //수의대
        LatLng(37.466197, 126.954146),
        LatLng(37.46602, 126.9522), //경영대
        LatLng(37.465959, 126.950842),
        LatLng(37.465546, 126.948513),
        LatLng(37.46577, 126.9484), //정문`
    )

    fun showRoute(googleMap: GoogleMap) {
        // Show the route of the circular shuttle on the map

        googleMap.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(roadCoordinates)
                .width(10f) // Set the width of the line
                .color(Color.BLACK) // Set the color of the line
        )
    }


}