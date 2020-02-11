package com.example.ecographics.settings

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.widget.Button
import android.widget.ImageView
import com.example.ecographics.monthly.ClimateChange
import com.example.ecographics.R
import com.bumptech.glide.Glide


class AboutMonthly : AppCompatActivity() {

    lateinit var nextButton: Button
    lateinit var backButton: Button
    lateinit var goToActivityButton: Button
    lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        }
        else {
            setTheme(R.style.lightThemeSettings)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_monthly)

        setTitle(R.string.tutorialTitle)

        val imageMonth = findViewById<ImageView>(R.id.about_monthly)

        Glide.with(this)
            .load(R.drawable.monthly)
            .into(imageMonth)

        exitButton = findViewById(R.id.exit)
        exitButton.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        nextButton = findViewById(R.id.next)
        nextButton.setOnClickListener {
            startActivity(Intent(this, AboutYearly::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        backButton = findViewById(R.id.back)
        backButton.setOnClickListener {
            startActivity(Intent(this, AboutSearch::class.java))
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        goToActivityButton = findViewById(R.id.goToActivity)
        goToActivityButton.setOnClickListener {
            startActivity(Intent(this, ClimateChange::class.java))
        }
    }
}
