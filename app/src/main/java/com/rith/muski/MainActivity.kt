package com.rith.muski

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavController: NavController
    private lateinit var secondaryNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setNavs()
    }

    private fun setNavs() {
        val genericNavHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val bottomNavHost = supportFragmentManager.findFragmentById(R.id.bottom_nav_host) as NavHostFragment

        secondaryNavController = genericNavHost.navController
        bottomNavController = bottomNavHost.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    showGenericHost()
                    secondaryNavController.navigate(R.id.dashboardFragment)
                    true
                }

                R.id.userList -> {
                    showBottomHost()
                    bottomNavController.navigate(R.id.userListFragment2)
                    true
                }

                R.id.profile -> {
                    showBottomHost()
                    bottomNavController.navigate(R.id.profileFragment2)
                    true
                }

                else -> false
            }

        }
    }
    private fun showGenericHost() {
        findViewById<View>(R.id.fragmentContainerView2).visibility = View.VISIBLE
        findViewById<View>(R.id.bottom_nav_host).visibility = View.GONE
    }

    private fun showBottomHost() {
        findViewById<View>(R.id.fragmentContainerView2).visibility = View.GONE
        findViewById<View>(R.id.bottom_nav_host).visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        val isGenericVisible = findViewById<View>(R.id.fragmentContainerView2).visibility == View.VISIBLE

        if (isGenericVisible) {
            val currentDestination = secondaryNavController.currentDestination?.id

            if (currentDestination == R.id.dashboardFragment) {
                // 🔥 Exit if on dashboard
                finish()
            } else {
                // ⬅️ Navigate back in generic stack
                if (!secondaryNavController.popBackStack()) finish()
            }
        } else {
            // ❌ No backstack for bottom nav → Exit
            super.onBackPressed()
        }
    }

}