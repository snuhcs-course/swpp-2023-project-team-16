package com.example.shattle.ui.circular

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.shattle.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CircularUI(
    val bt_refreshButton: Button,
    val tv_updatedTime: TextView,
    val context: Context?
) {
    var busMarkers: MutableList<Marker> = mutableListOf()

    val circularUtils = CircularUtils();

    fun updateUI(googleMap: GoogleMap?, circularUIState: CircularUIState) {

        drawCurrentLocationsOfBus(googleMap, circularUIState)
        changeUpdatedDateTime(circularUIState)
    }

    fun drawCurrentLocationsOfBus(googleMap: GoogleMap?, circularUIState: CircularUIState) {

        // Remove previous bus markers
        if (busMarkers.size != 0) {
            for (busMarker in busMarkers) {
                busMarker.remove()
            }
            busMarkers.clear()
        }

        val buses = circularUIState.buses

        val customMarkerIcon =
            circularUtils.bitmapDescriptorFromVector(context!!, R.drawable.img_circular_bus)
        // 20dp

        // Add bus markers on the map ("currentBusLocations" holds the locations)
        for (bus in buses) {
            val location = LatLng(bus.latitude, bus.longitude)
            val busMarker = googleMap?.addMarker(
                MarkerOptions()
                    .position(location) // Set the bus's initial position
                    .icon(customMarkerIcon) // Use a custom bus icon
                    .zIndex(1.0f)
            )
            if (busMarker != null) {
                busMarkers.add(busMarker)
            }
        }

    }

    fun changeUpdatedDateTime(circularUIState: CircularUIState) {
        //val dateTimeString = circularUIState.updatedTime
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSXXX", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        //val dateTime = inputFormat.parse(dateTimeString)
        val outputFormat =
            SimpleDateFormat("MM.dd hh:mm:ss (a)", Locale.getDefault()) //(hh 대신 HH 하면 24시간기준)
        //tv_updatedTime.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
    }

    fun customizeGoogleMap(googleMap: GoogleMap?) {

        // set initial camera location
        val initialLocation = LatLng(37.45800, 126.9531)
        val zoomLevel = 14.70f
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, zoomLevel))

        // 카메라 zoom level 설정
        googleMap?.setMaxZoomPreference(15.90f)
        googleMap?.setMinZoomPreference(14.50f)

        // 카메라 경계 설정 (남서, 북동)
        val bounds = LatLngBounds(
            LatLng(37.452692, 126.951310),
            LatLng(37.465542, 126.959983)
        )
        googleMap?.setLatLngBoundsForCameraTarget(bounds)

        // 버스 경로 표시
        drawRouteOfBus(googleMap)

        // 버스 정류장 표시
        drawBusStopLocations(googleMap)

        // 경로 위에 화살표 그리기
        drawRouteDirections(googleMap)

        // 예상 소요시간 표시
        //drawSectionDuration(googleMap)

    }

    fun drawRouteOfBus(googleMap: GoogleMap?) {
        // Show the route of the circular shuttle on the map

        // 경로 선 표시
        googleMap?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(circularUtils.roadCoordinates)
                .width(20.0f) // Set the width of the line
                .color(
                    ContextCompat.getColor(
                        context!!,
                        R.color.polyline_route_color
                    )
                ) // Set the color of the line
                .zIndex(-3.0f)
        )

    }

    fun drawBusStopLocations(googleMap: GoogleMap?) {
        // Show the bus stop locations on the map

        // 해당 벡터 파일을 bitmap 이미지로 변경 (마커 이미지가 bitmap 만 지원됨)
        val customMarkerIcon =
            circularUtils.bitmapDescriptorFromVector(context!!, R.drawable.img_circular_bus_stop)
        // 현재 18 dp 이미지 사용중

        for (busStop in circularUtils.busStops) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(busStop.location)
                    .title(busStop.title) // Set a title for the marker
                    //.snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
                    .zIndex(-1.0f)
            )
        }
    }

    fun drawRouteDirections(googleMap: GoogleMap?) {

        for (directionData in circularUtils.arrowData){
            val start = directionData.position
            val end = directionData.direction
            val bearing = circularUtils.bearingBetweenLocations(start, end)

            // 경로 사이사이에 방향 표시
            val customMarkerIcon =
                circularUtils.bitmapDescriptorFromVector(
                    context!!,
                    R.drawable.img_circular_load_direction
                )
            googleMap?.addMarker(
                MarkerOptions()
                    .position(directionData.position)
                    //.snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
                    .rotation(bearing)
                    .zIndex(-2.0f)
            )
        }
    }

    fun drawSectionDuration(googleMap: GoogleMap?) {
        // 커스텀 레이아웃을 사용하여 TextView를 생성합니다.
        val textView = TextView(context).apply {
            text = "Hello World!"
            textSize = 40f
        }

        // TextView를 Bitmap으로 변환합니다.
        val bitmap = circularUtils.createBitmapFromView(context!!, textView)

        // Bitmap을 사용하여 마커를 생성합니다.
        val marker = googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(37.45800, 126.9531))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .zIndex(-1.0f)
        )
    }

}