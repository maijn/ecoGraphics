package com.example.ecographics.settings

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.widget.Button
import android.widget.ImageView
import com.example.ecographics.R
import com.example.ecographics.search.Search
import com.bumptech.glide.Glide


class AboutSearch : AppCompatActivity() {

    lateinit var nextButton: Button
    lateinit var goToActivityButton: Button
    lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        //DARKMODE CHECK
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        }
        else {
            setTheme(R.style.lightThemeSettings)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_search)

        setTitle(R.string.tutorialTitle)

        val imageSearch = findViewById<ImageView>(R.id.about_search)

        Glide.with(this)
            .load(R.drawable.search)
            .into(imageSearch)


        exitButton = findViewById(R.id.exit)
        exitButton.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        nextButton = findViewById(R.id.next)
        nextButton.setOnClickListener {
            startActivity(Intent(this, AboutMonthly::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        goToActivityButton = findViewById(R.id.goToActivity)
        goToActivityButton.setOnClickListener {
            startActivity(Intent(this, Search::class.java))
        }



    }
}
