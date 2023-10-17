package com.example.shattle.ui.circular

import com.google.android.gms.maps.model.LatLng

data class CircularData(val isCreated: Boolean){

    data class BusStop(val position: LatLng, val title: String, val snippet: String)

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


    val roadCoordinates = listOf(
        LatLng(37.46577, 126.9484), //정문
        LatLng(37.46306, 126.9490), //법과대
        LatLng(37.46046, 126.9490), //자연대
        LatLng(37.45703, 126.9493), //농생대
        LatLng(37.45502, 126.9498), //38동, 공대입구
        LatLng(37.45356, 126.9502), //신소재
        LatLng(37.44809, 126.9521), //302동
        LatLng(37.45158, 126.9526), //301동, 유회진학술정보관
        LatLng(37.45408, 126.9539), //유전공학연구소
        LatLng(37.45612, 126.9554), //교수회관입구
        LatLng(37.46103, 126.9565), //기숙사삼거리
        LatLng(37.46418, 126.9553), //국제대학원
        LatLng(37.46606, 126.9546), //수의대
        LatLng(37.46602, 126.9522), //경영대
        LatLng(37.46577, 126.9484), //정문`
    )


}
