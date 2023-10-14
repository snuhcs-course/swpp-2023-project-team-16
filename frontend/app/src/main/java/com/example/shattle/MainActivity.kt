package com.example.shattle

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shattle.databinding.ActivityMainBinding
import com.example.shattle.ui.info.InfoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_station, R.id.navigation_circular, R.id.navigation_lostnfound, R.id.navigation_timetable,
//            ),
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        
        navView.setupWithNavController(navController)

        var isInfoOpened = false
        val infoFragment = InfoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.infoContainer, infoFragment)
        transaction.hide(infoFragment)
        transaction.commit()
        binding.infoButton.setOnClickListener{
            isInfoOpened = showInfo(isInfoOpened, infoFragment)
        }
        // info 팝업
    }

    private fun showInfo(isInfoOpened: Boolean, infoFragment: InfoFragment): Boolean{
        // TODO : 뒤로가기 버튼이나 클릭 시 hide, 팝업 밖 공간 클릭 시 hide
        // TODO : x 버튼? 좌측상단 << 버튼?

        val transaction = supportFragmentManager.beginTransaction()
        return if (isInfoOpened){
            transaction.hide(infoFragment)
            transaction.commit()
            false
        } else {
            transaction.show(infoFragment)
            transaction.commit()
            true
        }

    }

}