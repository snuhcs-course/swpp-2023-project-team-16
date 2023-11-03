package com.example.shattle

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shattle.databinding.ActivityMainBinding
import com.example.shattle.ui.info.InfoDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀 네비게이션 설정
        // activity_main.xml 의 navView 를 네비게이션 메뉴로 설정 (app:menu="@menu/bottom_nav_menu")
        val navView: BottomNavigationView = binding.navView
        // activity_main.xml 의 nav_host_fragment_activity_main 에 네비게이션 컨트롤러 설정 (app:navGraph="@navigation/mobile_navigation")
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Info 팝업 생성
        showInfo(supportFragmentManager)

        // TODO Info 열려있을 때 버튼 비활성화하기
        for (menu in navView.menu) menu.isEnabled = true

    }

    private fun showInfo(
        supportFragmentManager: FragmentManager
    ) {

        // Info 버튼을 누르면 InfoFragment 생성
        binding.infoImageButton.setOnClickListener {
            // 다른 팝업이 열려있는지 확인 (닫혀있다면 null 이 할당됨)
            val existingFragmentInfo = supportFragmentManager.findFragmentByTag("InfoFragment")
            // 다른 팝업이 닫혀 있는 경우에만 열기

            if((existingFragmentInfo != null) ){

            }
            else {
                val infoDialog = InfoDialog()
                val transaction = supportFragmentManager.beginTransaction()
                // infoFragment 가 표시될 위치 설정 (infoContainer), tag 설정(tag 로 fragment 가 열려있는지 확인 가능)
                transaction.add(R.id.infoContainer, infoDialog, "InfoFragment")
                // transaction 은 생성 할 때마다 commit 해주기
                transaction.commit()
            }
        }
    }

}