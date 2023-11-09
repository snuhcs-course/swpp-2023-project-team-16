package com.example.shattle.ui.circular

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.shattle.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CircularUI(
    val bt_refresh: Button,
    val tv_updatedTime: TextView,
    val bt_gps: ImageButton,
    val bt_home: ImageButton,
) {
    var busMarkers: MutableList<Marker> = mutableListOf()

    val circularUtils = CircularUtils();

    var userLocationMarker: Marker? = null

    val bounds = LatLngBounds(
        LatLng(37.445656, 126.945219), // 남서
        LatLng(37.469107, 126.958641) // 북동
    )

    val initialLocation = LatLng(37.45800, 126.9531)
    val initialZoomLevel = 14.70f

    var userLocation: LatLng? = null

    fun drawUserLocation(
        googleMap: GoogleMap?,
        context: Context,
        activity: Activity,
        fusedLocationClient: FusedLocationProviderClient,
        permissionCode: Int
    ) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한 요청
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }

        // 위치 서비스 활성화 확인 및 처리 로직은 여기에 추가...

        val customMarkerIcon =
            circularUtils.bitmapDescriptorFromVector(context, R.drawable.img_user_loaction)
        // 마지막 알려진 위치 가져오기
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                location?.let {
                    // 이전 마커 제거
                    userLocationMarker?.remove()
                    // 사용자의 현재 위치에 마커 추가
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    userLocationMarker = googleMap?.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .icon(customMarkerIcon)
                        // 아이콘 설정 등...
                    )
                    // 카메라를 기본 위치로 이동
                    googleMap?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            userLatLng,
                            initialZoomLevel
                        )
                    )
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
    }

    fun isUserLocationInBound(): Boolean {
        if (userLocation != null) {
            if (userLocation!!.latitude > bounds.northeast.latitude ||
                userLocation!!.longitude > bounds.northeast.longitude ||
                userLocation!!.latitude < bounds.southwest.latitude ||
                userLocation!!.longitude < bounds.southwest.longitude)
                return false
        }
        return true
    }

    fun resetCamera(googleMap: GoogleMap?){
        val currentLatLng = initialLocation
        val zoomLevel = initialZoomLevel
        val tilt = 0.0f
        val bearing = 0.0f

        val cameraPosition = CameraPosition.Builder()
            .target(currentLatLng)
            .zoom(zoomLevel)
            .bearing(bearing)
            .tilt(tilt)
            .build()
        
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun updateUI(googleMap: GoogleMap?, circularUIState: CircularUIState, context: Context) {

        drawCurrentLocationsOfBus(googleMap, circularUIState, context)
        changeUpdatedDateTime(circularUIState)
    }

    fun drawCurrentLocationsOfBus(
        googleMap: GoogleMap?,
        circularUIState: CircularUIState,
        context: Context
    ) {

        // Remove previous bus markers
        if (busMarkers.size != 0) {
            for (busMarker in busMarkers) {
                busMarker.remove()
            }
            busMarkers.clear()
        }

        val buses = circularUIState.buses

        val customMarkerIcon =
            circularUtils.bitmapDescriptorFromVector(context, R.drawable.img_circular_bus)
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

    fun customizeGoogleMap(googleMap: GoogleMap?, context: Context) {

        val style = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        googleMap?.setMapStyle(style)

        // set initial camera location
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, initialZoomLevel))

        // 카메라 zoom level 설정
        //googleMap?.setMaxZoomPreference(15.90f)
        googleMap?.setMinZoomPreference(14.50f)

        // 카메라 경계 설정 (남서, 북동)

        googleMap?.setLatLngBoundsForCameraTarget(bounds)

        // 버스 경로 표시
        drawRouteOfBus(googleMap, context)

        // 버스 정류장 표시
        drawBusStopLocations(googleMap, context)

        // 경로 위에 화살표 그리기
        drawRouteDirections(googleMap, context)

        // 예상 소요시간 표시
        //drawSectionDuration(googleMap)

    }

    fun drawRouteOfBus(googleMap: GoogleMap?, context: Context) {
        // Show the route of the circular shuttle on the map

        // 경로 선 표시
        googleMap?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(circularUtils.roadCoordinates)
                .width(20.0f) // Set the width of the line
                .color(
                    ContextCompat.getColor(
                        context,
                        R.color.polyline_route_color
                    )
                ) // Set the color of the line
                .zIndex(-3.0f)
        )

    }

    fun drawBusStopLocations(googleMap: GoogleMap?, context: Context) {
        // Show the bus stop locations on the map

        // 해당 벡터 파일을 bitmap 이미지로 변경 (마커 이미지가 bitmap 만 지원됨)
        val customMarkerIcon =
            circularUtils.bitmapDescriptorFromVector(context, R.drawable.img_circular_bus_stop)
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

    fun drawRouteDirections(googleMap: GoogleMap?, context: Context) {

        for (directionData in circularUtils.arrowData) {
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

    fun drawSectionDuration(googleMap: GoogleMap?, context: Context) {
        // 커스텀 레이아웃을 사용하여 TextView를 생성합니다.
        val textView = TextView(context).apply {
            text = "Hello World!"
            textSize = 40f
        }

        // TextView를 Bitmap으로 변환합니다.
        val bitmap = circularUtils.createBitmapFromView(context, textView)

        // Bitmap을 사용하여 마커를 생성합니다.
        val marker = googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(37.45800, 126.9531))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .zIndex(-1.0f)
        )
    }

}