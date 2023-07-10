package com.example.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationViews.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.breakingNewsFragment)
                    true
                }
                R.id.navigation_discovery -> {
                    navController.navigate(R.id.fragmentDiscovery)
                    true
                }
                R.id.navigation_save -> {
                    navController.navigate(R.id.savedNewsFragment)
                    true
                }
                R.id.navigation_settings -> {
                    navController.navigate(R.id.fragmentSettings)
                    true
                }
                else -> false
            }
        }

        // Hide BottomNavigationView in articleFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.articleFragment) {
                binding.bottomNavigationViews.isVisible = false
            } else {
                binding.bottomNavigationViews.isVisible = true
            }
        }
    }
}
