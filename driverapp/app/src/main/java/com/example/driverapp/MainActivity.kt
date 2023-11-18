package com.example.driverapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.driverapp.request.RequestBusIsRunning
import com.example.driverapp.request.RequestBusLocation
import com.example.driverapp.response.ResponseBusIsRunning
import com.example.driverapp.response.ResponseBusLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var gpsTracker: GPSTracker

    private lateinit var getBusButton: Button
    private lateinit var responseTextView: TextView
    private lateinit var stateTextView: TextView
    private lateinit var updateLocationButton: Button
    private lateinit var updateIsRunningButton: Button
    private lateinit var licensePlateText: EditText
    private lateinit var busLicensePlate: TextView
    private lateinit var busLatitude: TextView
    private lateinit var busLongitude: TextView
    private lateinit var busIsRunning: TextView
    private var isRunning: Boolean = false
    private var isTracking: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        responseTextView = findViewById(R.id.response)
        stateTextView = findViewById(R.id.state)
        licensePlateText= findViewById(R.id.licensePlateText)
        getBusButton = findViewById(R.id.getBusButton)
        getBusButton.setOnClickListener { getBusWithLicensePlate()}

        updateLocationButton = findViewById(R.id.updateLocationButton)
        updateLocationButton.setOnClickListener { toggleTracking() }

        updateIsRunningButton = findViewById(R.id.updateIsRunningButton)
        updateIsRunningButton.setOnClickListener { updateIsRunning() }


        busLicensePlate = findViewById(R.id.busLicensePlate)
        busLatitude = findViewById(R.id.busLatitude)
        busLongitude = findViewById(R.id.busLongitude)
        busIsRunning = findViewById(R.id.busIsRunning)




        // Request location permissions if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }

        gpsTracker = GPSTracker(this)
    }

    private fun toggleTracking() {
        if(isRunning){
            if (!isTracking) {
                // Stop GPS data submission
                isTracking = true
                stateTextView.text = "5초마다 위치 새로고침 중"
                updateLocationButton.text = "위치 추적 끄기"
                startGPSDataSubmission()
            } else {
                // Start GPS data submission
                isTracking = false
                stateTextView.text = "위치 새로고침 종료됨"
                updateLocationButton.text = "위치 추적 켜기"
                //fixme: put 요청 종료 필요
            }
        }else{
            if(!isTracking){
                stateTextView.text = "미운행중"
                updateLocationButton.text = "미운행중"
            }else{
                isTracking=false
                stateTextView.text = "미운행중"
                updateLocationButton.text = "미운행중"
                //fixme: put 요청 종료 필요
            }

        }

    }

    private fun startGPSDataSubmission() {
        val handler = Handler()
        val delay = 5000L  // 5 seconds

        handler.post(object : Runnable {
            override fun run() {
                val location: Location? = gpsTracker.getLocation()
                val licensePlate: String = licensePlateText.text.toString()
                if (location != null) {
                    // Construct a GPSData object
                    val gpsData = GPSData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        licensePlate = licensePlate,
                    )

                    // Send the GPSData object to the endpoint
                    updateBusLocation(gpsData)

                    // Schedule the next submission
                    handler.postDelayed(this, delay)
                }
            }
        })
    }

    private fun getBusWithLicensePlate() {
        val licensePlate: String = licensePlateText.text.toString()
        val call: Call<ResponseBusLocation> = ServiceCreator.busGetService.getBusLocation(
            licensePlate = licensePlate
        )  // ResponseBusLocation 을 받아오는 객체

        call.enqueue(object : Callback<ResponseBusLocation> { // ResponseBusLocation 를 받아왔을 때의 행동
            override fun onResponse(
                call: Call<ResponseBusLocation>,
                response: Response<ResponseBusLocation>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null){
                        isRunning = body.isRunning
                    }
                    // Handle a successful response from the server
                    responseTextView.text = "버스 정보 얻기: ${body?.latitude.toString()}, ${body?.longitude.toString()}"
                    busLicensePlate.text = "${body?.licensePlate}"
                    busLatitude.text = "위도: ${body?.latitude.toString()}"
                    busLongitude.text = "경도: ${body?.longitude.toString()}"
                    busIsRunning.text = "운행 중: ${body?.isRunning.toString()}"


                } else {
                    Toast.makeText(this@MainActivity,
                        "${response.code()}: 내 버스 불러오기 실패", Toast.LENGTH_SHORT).show()
                    // Handle errors or unsuccessful responses
                }
            }

            override fun onFailure(call: Call<ResponseBusLocation>, t: Throwable) {
                // Handle network failures or request exceptions
                Log.e("NetworkTest", "error: $t")
            }
        })
    }
    private fun updateIsRunning(){
        val requestBusIsRunning = RequestBusIsRunning(
            licensePlate = licensePlateText.text.toString(),
            isRunning = !isRunning
        )

        val call: Call<ResponseBusIsRunning> = ServiceCreator.busUpdateService.updateBusIsRunning(
            requestBusIsRunning
        )  // ResponseBusIsRunning 을 받아오는 객체

        call.enqueue(object : Callback<ResponseBusIsRunning> { // ResponseBusIsRunning 를 받아왔을 때의 행동
            override fun onResponse(
                call: Call<ResponseBusIsRunning>,
                response: Response<ResponseBusIsRunning>
            ) {
                if (response.isSuccessful) {
                    isRunning = !isRunning
                    toggleTracking()
                    // Handle a successful response from the server
                    val body = response.body()
                    responseTextView.text = "운행 중 변경하기: ${body?.isRunning.toString()}"
                    busIsRunning.text = "운행 중: ${body?.isRunning.toString()}"
                } else {
                    Toast.makeText(this@MainActivity,
                        "${response.code()}: 운행 중 변경 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBusIsRunning>, t: Throwable) {
                // Handle network failures or request exceptions
                Log.e("NetworkTest", "error: $t")
            }
        })
    }
    private fun updateBusLocation(data: GPSData) {
        val requestBusLocation = RequestBusLocation(
            licensePlate = data.licensePlate,
            latitude = data.latitude,
            longitude = data.longitude
        )


        val call: Call<ResponseBusLocation> = ServiceCreator.busUpdateService.submitGPSData(
            requestBusLocation
        )  // ResponseBusLocation 을 받아오는 객체

        call.enqueue(object : Callback<ResponseBusLocation> { // ResponseBusLocation 를 받아왔을 때의 행동
            override fun onResponse(
                call: Call<ResponseBusLocation>,
                response: Response<ResponseBusLocation>
            ) {
                if (response.isSuccessful) {
                    // Handle a successful response from the server
                    val body = response.body()
                    // Handle a successful response from the server
                    responseTextView.text = "위치 변경하기: ${body?.latitude.toString()}, ${body?.longitude.toString()}"
                    busLicensePlate.text = "${body?.licensePlate}"
                    busLatitude.text = "위도: ${body?.latitude.toString()}"
                    busLongitude.text = "경도: ${body?.longitude.toString()}"
                    busIsRunning.text = "운행 중: ${body?.isRunning.toString()}"

                } else {
                    Toast.makeText(this@MainActivity,
                        "${response.code()}: 위치 변경 실패", Toast.LENGTH_SHORT).show()
                    // Handle errors or unsuccessful responses
                }
            }

            override fun onFailure(call: Call<ResponseBusLocation>, t: Throwable) {
                // Handle network failures or request exceptions
                Log.e("NetworkTest", "error: $t")
            }
        })
    }





}

