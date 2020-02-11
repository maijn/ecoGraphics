package com.example.ecographics.monthly

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem
import com.example.ecographics.daily.DailyTemperature
import com.example.ecographics.R

//import com.github.sanity.pav.PairAdjacentViolators.*

class ClimateChange : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {


    fun loadFragment(fragment: Fragment?): Boolean{
        if (fragment != null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
            return true
        }
        return false

    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
         var fragment: Fragment? = null

        when(p0.itemId){
            R.id.temperatur -> fragment = MonthlyTmpFragment()
            R.id.nedbør -> fragment = MonthlyRainFragment()
        }

        return loadFragment(fragment)

    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        } else {
            setTheme(R.style.lightThemeMonthly)
        }


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_climatechange)



        val home_button = findViewById<FloatingActionButton>(R.id.fab)
        home_button.setOnClickListener {
            val intent = Intent(this, DailyTemperature::class.java)
            startActivity(intent)
        }

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        navigation.setOnNavigationItemSelectedListener(this)


        loadFragment(MonthlyTmpFragment())
    }
}
