package com.nd.shattle.ui.circular

import android.content.Context
import android.location.Location
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nd.shattle.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CircularUI(
    val context: Context,
    val bt_refresh: Button,
    val tv_updatedTime: TextView,
    val bt_gps: ImageButton,
) {
    var busMarkers: MutableList<Marker> = mutableListOf()

    var userLocationMarker: Marker? = null
    var userLocation: LatLng? = null

    val cameraBounds = LatLngBounds(
        LatLng(37.445656, 126.945219), // 남서
        LatLng(37.468911, 126.959790) // 북동
    )

    val initialLocation = LatLng(37.45500, 126.9531)
    val initialZoomLevel = 14.70f

    val maxZoomLevel = 18.50f // 17.50f
    val minZoomLevel = 14.50f

    val markersWithInitialRotation = mutableListOf<Pair<Marker, Float>>()

    fun updateUI(googleMap: GoogleMap?, circularUIState: CircularUIState) {
        drawCurrentLocationsOfBus(googleMap, circularUIState)
        changeUpdatedDateTime(circularUIState)
    }

    fun drawCurrentLocationsOfBus(
        googleMap: GoogleMap?,
        circularUIState: CircularUIState,
    ) {
        // 이전 마커 제거
        if (busMarkers.size != 0) {
            for (busMarker in busMarkers) {
                busMarker.remove()
            }
            busMarkers.clear()
        }

        val buses = circularUIState.buses

        val customMarkerIcon =
            CircularUtils.bitmapDescriptorFromVector(context, R.drawable.img_circular_bus)

        // 현재 버스들 위치에 마커 추가
        for (bus in buses) {
            val location = LatLng(bus.latitude, bus.longitude)
            val busMarker = googleMap?.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(customMarkerIcon)
                    .anchor(0.5f, 0.5f)
                    .zIndex(2.0f)
            )
            if (busMarker != null) {
                busMarkers.add(busMarker)
            }
        }
    }

    fun changeUpdatedDateTime(circularUIState: CircularUIState) {
        try {
            val dateTimeString = circularUIState.updatedTime
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            //val dateTime = inputFormat.parse(dateTimeString)
            val dateTime = Date()
            val outputFormat = SimpleDateFormat("MM.dd HH:mm:ss", Locale.getDefault())
            tv_updatedTime.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
        } catch (e: Exception) {
            tv_updatedTime.text = ""
        }

    }

    fun drawUserLocation(
        googleMap: GoogleMap?,
        location: Location
    ) {

        // 이전 마커 제거
        userLocationMarker?.remove()

        // 사용자의 현재 위치에 마커 추가
        val userLatLng = LatLng(location.latitude, location.longitude)
        val customMarkerIcon =
            CircularUtils.bitmapDescriptorFromVector(context, R.drawable.img_user_loaction)
        userLocationMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(userLatLng)
                .icon(customMarkerIcon)
                .anchor(0.5f, 0.5f)
                .zIndex(2.1f)
        )

        // 지도 카메라 업데이트
        val currentCameraPosition = googleMap?.cameraPosition
        val cameraPosition = CameraPosition.Builder()
            .target(userLatLng)
            .zoom(currentCameraPosition?.zoom ?: initialZoomLevel)
            .bearing(currentCameraPosition?.bearing ?: 0f)
            .tilt(currentCameraPosition?.tilt ?: 0f)
            .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // 현재 위치 저장
        userLocation = LatLng(location.latitude, location.longitude)
    }

    fun customizeGoogleMap(googleMap: GoogleMap?) {

        val style = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        googleMap?.setMapStyle(style)

        // 초기 카메라 위치
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, initialZoomLevel))

        // 카메라 max/min zoom level 설정
        googleMap?.setMaxZoomPreference(maxZoomLevel)
        googleMap?.setMinZoomPreference(minZoomLevel)

        // 카메라 경계 설정 (남서, 북동)
        googleMap?.setLatLngBoundsForCameraTarget(cameraBounds)

        // 버스 경로 표시
        drawRouteOfBus(googleMap)

        // 경로 위에 화살표 그리기
        drawRouteDirections(googleMap)

        // 버스 정류장 표시
        drawBusStopLocations(googleMap)
    }


    fun drawRouteOfBus(googleMap: GoogleMap?) {

        // 경로 선 표시
        googleMap?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(CircularUtils.roadCoordinates)
                .width(23.0f)
                .color(
                    ContextCompat.getColor(
                        context,
                        R.color.polyline_route
                    )
                ) // Set the color of the line
                .zIndex(1.0f)
        )

        // 경로 위 흰색 테두리
        googleMap?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(CircularUtils.roadCoordinates)
                .width(31.0f) // Set the width of the line
                .color(
                    ContextCompat.getColor(
                        context,
                        R.color.polyline_route_stroke
                    )
                ) // Set the color of the line
                .zIndex(0.9f)
        )

    }


    fun drawRouteDirections(googleMap: GoogleMap?) {

        for (directionData in CircularUtils.arrowData) {
            val start = directionData.position
            val end = directionData.direction
            val bearing = CircularUtils.bearingBetweenLocations(start, end)

            // 경로 사이사이에 방향 표시
            val customMarkerIcon =
                CircularUtils.bitmapDescriptorFromVector(
                    context,
                    R.drawable.img_circular_route_direction
                )
            val marker = googleMap?.addMarker(
                MarkerOptions()
                    .position(directionData.position)
                    //.snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
                    .anchor(0.5f, 0.5f)
                    .rotation(bearing)
                    .zIndex(1.1f)
            )
            if (marker != null) {
                markersWithInitialRotation.add(Pair(marker, bearing))
            }

        }

        googleMap?.setOnCameraMoveListener {
            val mapRotation = googleMap.cameraPosition.bearing
            for ((marker, initialRotation) in markersWithInitialRotation) {
                // 마커의 최종 회전 각도는 지도의 회전 각도와 초기 회전 각도의 합입니다.
                marker.rotation = -mapRotation + initialRotation
            }
        }

    }

    fun drawBusStopLocations(googleMap: GoogleMap?) {
        // Show the bus stop locations on the map

        // 해당 벡터 파일을 bitmap 이미지로 변경 (마커 이미지가 bitmap 만 지원됨)
        val customMarkerIcon =
            CircularUtils.bitmapDescriptorFromVector(context, R.drawable.img_circular_bus_stop)

        for (busStop in CircularUtils.busStops) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(busStop.location)
                    .title(busStop.title) // Set a title for the marker
                    //.snippet(busStop.snippet) // Set additional information
                    .icon(customMarkerIcon) // Set a custom marker icon (optional)
                    .anchor(0.5f, 0.5f)
                    .zIndex(1.2f)
            )
        }
    }
}