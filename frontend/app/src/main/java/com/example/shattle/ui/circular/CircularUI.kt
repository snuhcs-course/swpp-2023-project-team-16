package com.example.shattle.ui.circular

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.IntentSender
import com.example.shattle.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CircularUI(
    val bt_refresh: Button,
    val tv_updatedTime: TextView,
    val bt_gps: ImageButton,
) {
    var busMarkers: MutableList<Marker> = mutableListOf()

    val circularUtils = CircularUtils();

    var userLocationMarker: Marker? = null

    val bounds = LatLngBounds(
        LatLng(37.445656, 126.945219), // 남서
        LatLng(37.468911, 126.959790) // 북동
    )

    val initialLocation = LatLng(37.45500, 126.9531)
    val initialZoomLevel = 14.70f

    val maxZoomLevel = 18.50f // 17.50f
    val minZoomLevel = 14.50f

    var userLocation: LatLng? = null

    val markersWithInitialRotation = mutableListOf<Pair<Marker, Float>>()

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

        // 마지막 알려진 위치 가져오기
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // 위치 설정이 충족됨. 위치 업데이트를 요청할 수 있음.
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    location?.let {
                        // 이전 마커 제거
                        userLocationMarker?.remove()
                        // 사용자의 현재 위치에 마커 추가
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        val customMarkerIcon =
                            circularUtils.bitmapDescriptorFromVector(context, R.drawable.img_user_loaction)
                        userLocationMarker = googleMap?.addMarker(
                            MarkerOptions()
                                .position(userLatLng)
                                .icon(customMarkerIcon)
                                .anchor(0.5f, 0.5f)
                                .zIndex(2.1f)
                            // 아이콘 설정 등...
                        )

                        val currentCameraPosition = googleMap?.cameraPosition
                        val cameraPosition = CameraPosition.Builder()
                            .target(userLatLng)
                            .zoom(currentCameraPosition?.zoom ?: initialZoomLevel)
                            .bearing(currentCameraPosition?.bearing ?: 0f)
                            .tilt(currentCameraPosition?.tilt ?: 0f)
                            .build()

                        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                        userLocation = LatLng(it.latitude, it.longitude)
                    }
                }
        }
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
            val dateTime = inputFormat.parse(dateTimeString)
            val outputFormat = SimpleDateFormat("MM.dd HH:mm:ss", Locale.getDefault())
            tv_updatedTime.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
        } catch (e: Exception) {
            tv_updatedTime.text = ""
        }

    }

    fun isUserLocationInBound(): Boolean {
        if (userLocation != null) {
            if (userLocation!!.latitude > bounds.northeast.latitude ||
                userLocation!!.longitude > bounds.northeast.longitude ||
                userLocation!!.latitude < bounds.southwest.latitude ||
                userLocation!!.longitude < bounds.southwest.longitude
            )
                return false
        }
        return true
    }

    fun customizeGoogleMap(googleMap: GoogleMap?, context: Context) {

        val style = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        googleMap?.setMapStyle(style)

        // set initial camera location
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, initialZoomLevel))

        // 카메라 zoom level 설정
        googleMap?.setMaxZoomPreference(maxZoomLevel)
        googleMap?.setMinZoomPreference(minZoomLevel)

        // 카메라 경계 설정 (남서, 북동)
        googleMap?.setLatLngBoundsForCameraTarget(bounds)

        // 버스 경로 표시
        drawRouteOfBus(googleMap, context)

        // 경로 위에 화살표 그리기
        drawRouteDirections(googleMap, context)

        // 버스 정류장 표시
        drawBusStopLocations(googleMap, context)
    }


    fun drawRouteOfBus(googleMap: GoogleMap?, context: Context) {
        // Show the route of the circular shuttle on the map

        // 경로 선 표시
        googleMap?.addPolyline(
            PolylineOptions()
                .clickable(false)
                .addAll(circularUtils.roadCoordinates)
                .width(23.0f) // Set the width of the line
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
                .addAll(circularUtils.roadCoordinates)
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


    fun drawRouteDirections(googleMap: GoogleMap?, context: Context) {

        for (directionData in circularUtils.arrowData) {
            val start = directionData.position
            val end = directionData.direction
            val bearing = circularUtils.bearingBetweenLocations(start, end)

            // 경로 사이사이에 방향 표시
            val customMarkerIcon =
                circularUtils.bitmapDescriptorFromVector(
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

    fun drawBusStopLocations(googleMap: GoogleMap?, context: Context) {
        // Show the bus stop locations on the map

        // 해당 벡터 파일을 bitmap 이미지로 변경 (마커 이미지가 bitmap 만 지원됨)
        val customMarkerIcon =
            circularUtils.bitmapDescriptorFromVector(context, R.drawable.img_circular_bus_stop)

        for (busStop in circularUtils.busStops) {
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