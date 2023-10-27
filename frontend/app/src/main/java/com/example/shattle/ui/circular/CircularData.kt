package com.example.shattle.ui.circular

import android.util.Log
import com.example.shattle.data.models.CircularBus
import com.example.shattle.network.ServiceCreator
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

data class CircularData(val isCreated: Boolean) {

    data class BusStop(val position: LatLng, val title: String, val snippet: String)


    var currentBusLocations = listOf<CircularBus>()

    var dateTimeString = "2023-01-23T12:34:56Z"

    val dummy = listOf(
        listOf(
            CircularBus(UUID.randomUUID(), LatLng(37.46577, 126.9484)),
            CircularBus(UUID.randomUUID(), LatLng(37.44809, 126.9521))
        ),
        listOf(
            CircularBus(UUID.randomUUID(), LatLng(37.46306, 126.9490)),
            CircularBus(UUID.randomUUID(), LatLng(37.45158, 126.9526)),
        ),
        listOf(
            CircularBus(UUID.randomUUID(), LatLng(37.46046, 126.9490)),
            CircularBus(UUID.randomUUID(), LatLng(37.45408, 126.9539)),
        ),
        listOf(
            CircularBus(UUID.randomUUID(), LatLng(37.46046, 126.9490)),
            CircularBus(UUID.randomUUID(), LatLng(37.45408, 126.9539)),
        ),
        listOf(
            CircularBus(UUID.randomUUID(), LatLng(37.46046, 126.9490)),
            CircularBus(UUID.randomUUID(), LatLng(37.45408, 126.9539)),
        ),
        listOf(
            CircularBus(UUID.randomUUID(), LatLng(37.46046, 126.9490)),
            CircularBus(UUID.randomUUID(), LatLng(37.45408, 126.9539)),
        ),


    )
    var cnt = 0;
    fun refreshCurrentBusLocation() {
        if(cnt >= dummy.size)
            currentBusLocations = emptyList()
        else
            currentBusLocations = dummy[cnt++]
    }

    fun refreshCurrentBusLocation2() {
        val call: Call<List<CircularBus>> = ServiceCreator.apiService.getCircularLocation()

        call.enqueue(object : Callback<List<CircularBus>> {
            override fun onResponse(
                call: Call<List<CircularBus>>,
                response: Response<List<CircularBus>>
            ) {
                if (response.isSuccessful) {
                    val locations = response.body()
                    if (locations == null) {
                        Log.e("Circular", "error: response contained null data")
                    }
                    else {
                        currentBusLocations = locations
                    }
                } else {

                }
            }
            override fun onFailure(call: Call<List<CircularBus>>, t: Throwable) {
                Log.e("Circular", "error: $t")
            }
        })
    }

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

}
