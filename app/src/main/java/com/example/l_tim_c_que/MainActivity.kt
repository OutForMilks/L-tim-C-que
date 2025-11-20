package com.example.l_tim_c_que

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.l_tim_c_que.firebase.FirebaseDB
import com.example.l_tim_c_que.firebase.NetworkChecker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

/**
 * The main entry point of the application.
 * Hosts the navigation graph and the bottom navigation view.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDB.init(this)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        navHostFragment?.navController?.let { navController ->
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNav.setupWithNavController(navController)
        }
    }
}
