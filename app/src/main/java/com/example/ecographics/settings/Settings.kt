package com.example.ecographics.settings
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.widget.*
import com.example.ecographics.daily.DailyTemperature
import com.example.ecographics.R
import com.example.ecographics.utils.LocaleHelper
import kotlinx.android.synthetic.main.contact_dialog.view.*
import kotlinx.android.synthetic.main.dialog_terms_of_service.view.*
import java.lang.Exception
import com.example.ecographics.utils.Common

class Settings : AppCompatActivity() {

    lateinit var darkModeSwitch: Switch
    lateinit var about_ecoGraphics : Button
    lateinit var changeLanguageButton : Button
    lateinit var contactButton: Button
    lateinit var termsOfServiceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        //DARK MODE CHECK
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        }
        else {
            setTheme(R.style.lightThemeSettings)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val common:Common = Common()

        val home_icon = findViewById<ImageView>(R.id.sun_icon)

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            home_icon.setImageResource(R.drawable.ic_moon_outline)
            home_icon.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN)
        } else { //DED2FF
            home_icon.setImageResource(R.drawable.ic_sun_outline)
            home_icon.setColorFilter(Color.parseColor("#c9e3e6"), PorterDuff.Mode.SRC_IN)
        }

        val home_button = findViewById<FloatingActionButton>(R.id.fab)
        home_button.setOnClickListener {
            val intent = Intent(this, com.example.ecographics.daily.DailyTemperature::class.java)
            startActivity(intent)
        }

        about_ecoGraphics = findViewById<Button>(R.id.about_eco)
        about_ecoGraphics.setOnClickListener {
            val intent = Intent(this, AboutEcographics::class.java )
            startActivity(intent)
        }

        changeLanguageButton = findViewById<Button>(R.id.change_lang)
        changeLanguageButton.setOnClickListener {
            showChangeLanguageDialog()
        }

        darkModeSwitch = findViewById<Switch>(R.id.darkMode_switch)
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeSwitch.isChecked = true
        }
        darkModeSwitch.setOnCheckedChangeListener {_, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                restartApp()
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                restartApp()
            }
        }

        //TERMS OF SERVICE
        termsOfServiceButton = findViewById(R.id.termsOfService)
        termsOfServiceButton.setOnClickListener {
            val dialog_termsOfService = LayoutInflater.from(this).inflate(R.layout.dialog_terms_of_service, null)
            val builder = AlertDialog.Builder(this).setView(dialog_termsOfService).setTitle("Terms of Service")
            val alertDialog = builder.show()

            //DONE
            dialog_termsOfService.done.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        //KONTAKT OSS
        contactButton = findViewById(R.id.kontaktOss)
        contactButton.setOnClickListener {
            val dialog_mail = LayoutInflater.from(this).inflate(R.layout.contact_dialog, null)
            val builder = AlertDialog.Builder(this).setView(dialog_mail)
            val alertDialog = builder.show()

            //SEND
            dialog_mail.sendMail_button.setOnClickListener {
                alertDialog.dismiss()
                val title = dialog_mail.title_mail.text.toString().trim()
                val message = dialog_mail.message_mail.text.toString().trim()

                sendEmail(title, message)
            }
            //CANCEL
            dialog_mail.cancelMail_button.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    fun sendEmail(title: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto")
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("thu_thuan@hotmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(intent, "Choose Email Client"))
        }
        catch (e: Exception) {
            Toast.makeText(this, "En feil oppstod, prøv igjen", Toast.LENGTH_LONG).show()
        }
    }


    //hjelpefunksjon for change LANGUAGE
    private fun showChangeLanguageDialog() {
        lateinit var dialog: AlertDialog
        val languages = arrayOf("Norsk", "English","中文")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.velgSpråk))
        alertDialogBuilder.setSingleChoiceItems(languages, -1) { _, which ->
            val language = languages[which]
            if (language == "Norsk") {
                LocaleHelper.setLocale(this, "no")
                this.finish()
                val refresh = Intent(this, DailyTemperature::class.java)
                startActivity(refresh)
            }
            else if (language == "English") {
                LocaleHelper.setLocale(this, "en")
                this.finish()
                val refresh = Intent(this, DailyTemperature::class.java)
                startActivity(refresh)
            }
            else if (language == "中文") {
                LocaleHelper.setLocale(this, "zh")
                this.finish()
                val refresh = Intent(this, DailyTemperature::class.java)
                startActivity(refresh)
            }
            dialog.dismiss()
        }
        dialog = alertDialogBuilder.create()
        dialog.show()
    }

    //hjelpefunksjon for DARK MODE
     fun restartApp() {
        intent = Intent(applicationContext, DailyTemperature::class.java)
        startActivity(intent)
        finish()
    }
}
