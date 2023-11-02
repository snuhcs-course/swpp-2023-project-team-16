package com.example.shattle.ui.dropoff

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shattle.R
import com.example.shattle.databinding.FragmentDropoffBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone



class DropoffFragment : Fragment() {

    private lateinit var dropoffViewModel: DropoffViewModel
    var numberOfPeopleWaitingLine: Int? = null
    var numberOfNeededBus: Int? = null
    var waitingTimeInMin: Int? = null
    var dateTimeString: String? = null

    private var _binding: FragmentDropoffBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dropoffViewModel = ViewModelProvider(this).get(DropoffViewModel::class.java)

        _binding = FragmentDropoffBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setObserver()

        // 서버에서 받아오는 시간 때문에 0.5초 뒤 화면 변경
        // call 이 실패하거나 0.5초보다 오래 걸릴 경우 하단 새로고침 버튼 눌러야됨
        Handler(Looper.getMainLooper()).postDelayed({
            changeView()
        }, 500)


        refreshView()

        return root
    }

    fun setObserver(){
        dropoffViewModel.numberOfPeopleWaitingLine.observe(viewLifecycleOwner, Observer {
            numberOfPeopleWaitingLine = it
        })
        dropoffViewModel.numberOfNeededBus.observe(viewLifecycleOwner, Observer {
            numberOfNeededBus = it
        })
        dropoffViewModel.waitingTimeInMin.observe(viewLifecycleOwner, Observer {
            waitingTimeInMin = it
        })
        dropoffViewModel.dateTimeString.observe(viewLifecycleOwner, Observer {
            dateTimeString = it
        })
    }

    fun refreshData() {
        dropoffViewModel.refreshCurrentLineData()
    }

    fun changeView() {

        // 서버 호출이 성공한 경우에만 화면 업데이트, 호출 실패 시 toast 만 띄워주고 화면 업데이트 X
        if (dropoffViewModel.isSuccessCall) {
            changeVisualView()
            changeTextView()
            updateUpdatedDateTime()
        } else {
            Toast.makeText(activity, R.string.toast_refresh_error, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeVisualView() {
        val constraintLayout = binding.visualViewLayout

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Change the bias values as desired.
        var bias = (numberOfPeopleWaitingLine!! / 120.0).toFloat()
        // 정류장 이미지랑 겹쳐서 최소 bias 0.15로 설정
        if(bias < 0.15F)
            bias = 0.15F
        constraintSet.setHorizontalBias(R.id.manImageView, (bias))
        // Apply the updated constraints to the ConstraintLayout.
        constraintSet.applyTo(constraintLayout)
    }

    fun changeTextView() {
        binding.numPeople.text = "현재 ${numberOfPeopleWaitingLine}명 대기 중입니다."
        binding.numBus.text = "다음 ${numberOfNeededBus}번째 버스 탑승 가능합니다."
        binding.numMinute.text = "예상 대기시간: ${waitingTimeInMin}분"
    }

    fun updateUpdatedDateTime() {
        var inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = inputFormat.parse(dateTimeString)

        val outputFormat = SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())
        binding.updatedTimeTextView.text = "최종 업데이트 - ${outputFormat.format(dateTime)}"
    }

    fun refreshView() {

        // 1. Refresh manually
        binding.refreshButton.setOnClickListener {
            refreshData()
            changeView()
        }

        // 2. Refresh automatically
        val handler = Handler(Looper.getMainLooper())
        val refreshRunnable = object : Runnable {
            override fun run() {
                try {
                    refreshData()
                    changeView()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("MyLogChecker", "error: $e")
                }
                handler.postDelayed(this, 30000)
            }
        }
        // Start the automatic refresh
        handler.postDelayed(refreshRunnable, 30000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}