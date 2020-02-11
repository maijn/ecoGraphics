package com.example.ecographics.settings
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.example.ecographics.R
import kotlinx.android.synthetic.main.activity_about_ecographics.*

class AboutEcographics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //DARKMODE CHECK
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        }
        else {
            setTheme(R.style.lightThemeSettings)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_ecographics)

        val actionbar = supportActionBar
        actionbar!!.title = resources.getString(R.string.tutorialTitle)
        actionbar.setDisplayHomeAsUpEnabled(true)

        videre.setOnClickListener {
            startActivity(Intent(this, AboutSearch::class.java))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
