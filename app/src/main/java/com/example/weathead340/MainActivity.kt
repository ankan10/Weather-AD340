package com.example.weathead340

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weathead340.forecast.CurrentForecastFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tempDisplaySettingManager = TempDisplaySettingManager(this)



        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            .setTitle(R.string.app_name)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater =  menuInflater
        inflater.inflate(R.menu.settings_menu_xml, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when(item.itemId) {
            R.id.tempDisplaySeting -> {
                showTempDisplaySettingDialog(this, tempDisplaySettingManager)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}