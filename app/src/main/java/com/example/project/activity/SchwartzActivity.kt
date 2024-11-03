package com.example.project.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.project.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchwartzActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment)
            .navController
    }
}