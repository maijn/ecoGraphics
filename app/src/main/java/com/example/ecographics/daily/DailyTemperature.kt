package com.example.ecographics.daily

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.CardView
import android.widget.*
import com.bumptech.glide.Glide
import com.example.ecographics.R
import com.example.ecographics.search.Search
import com.example.ecographics.map.NorwayMap
import com.example.ecographics.models.*
import com.example.ecographics.monthly.ClimateChange
import com.example.ecographics.yearly.YearlyClimate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_dagens_temperatur.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

class DailyTemperature : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var location: Location
    val REQUEST_CODE = 1000
    internal lateinit var locationRequest: LocationRequest
    private var latti: String? = null
    private var longi: String? = null
    private lateinit var sources: String

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
            supportActionBar!!.hide()
        }
        else {
            //themeUtil.onActivityCreateSetTheme(this)
            setTheme(R.style.lightThemeStartPage)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagens_temperatur)
        changeTheme(6f,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            true)
        var locationtxtt = findViewById<TextView>(R.id.location_txt)
        locationtxtt.setText("EcoGraphics")

        // FIKSER BUTTONS
        /*val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener {
            val intent = Intent(this, Search::class.java)
            startActivity(intent)
        }*/

        val dailyCardView = findViewById<CardView>(R.id.daily)
        dailyCardView.setOnClickListener {
            val intent = Intent(this, Search::class.java)
            startActivity(intent)
        }

        val monthlyCardView = findViewById<CardView>(R.id.monthly)
        monthlyCardView.setOnClickListener {
            val intent = Intent(this, ClimateChange::class.java)
            startActivity(intent)
        }

        val mapCardView = findViewById<CardView>(R.id.map)
        mapCardView.setOnClickListener {
            val intent = Intent(this, NorwayMap::class.java)
            startActivity(intent)
        }

        val futureCardView = findViewById<CardView>(R.id.future)
        futureCardView.setOnClickListener {
            val intent = Intent(this, YearlyClimate::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<ImageButton>(R.id.button_settings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, com.example.ecographics.settings.Settings::class.java)
            startActivity(intent)
        }

        //supportActionBar!!.hide()

        //--------TILBAKE----------
        /*val actionbar = supportActionBar
        actionbar!!.setTitle(resources.getString(R.string.dagens))
        actionbar.setDisplayHomeAsUpEnabled(true)*/

        //--------CURRENT DATE----------------
        val textview_date = findViewById<TextView>(R.id.dato)
        val date = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(date.time)
        textview_date.text = currentDate

        //----------CURRENT LOCATION------------
        locationRequest = LocationRequest()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGsp()
        }
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }
    }

   /* override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    } */

    private fun buildAlertMessageNoGsp() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Slå på GPS")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                , REQUEST_CODE)
            }
            .setNegativeButton("No") {dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert = builder.create() as AlertDialog
        alert.show()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun startLocationUpdates() {
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.setInterval(10000)
        locationRequest!!.setFastestInterval(10000)
        locationRequest!!.setSmallestDisplacement(200f)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)

        val locationSettingRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingRequest)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun onLocationChanged(location: Location) {
        this.location = location

        latti = String.format(Locale.US,"%.3f", location.latitude)
        longi = String.format(Locale.US,"%.3f", location.longitude)

        //----API CALL----
        val geometry: String = "nearest(POINT(${longi} ${latti}))"

        //val geometry: String = "nearest(POINT(${longi} ${latti}))"

        val options = HashMap<String,String>()
        options.put("geometry",geometry)

        val service = RetrofitClientInstance.retrofitInstance?.create(
            GetSourceService::class.java)
        val call = service?.getAllData(options)

        call?.enqueue(object: Callback<DataListSources> {
            val locationtxt = findViewById<TextView>(R.id.location_txt)
            val celciustxt = findViewById<TextView>(R.id.location_txt)
            val darkModeOrNot = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

            //Dersom det failer med å hente data
            override fun onFailure(call: Call<DataListSources>, t: Throwable) {
                //Toast.makeText(applicationContext, "Error reading JSON! ${t}", Toast.LENGTH_LONG).show()
                //changeTheme(6f,darkModeOrNot,true)
                locationtxt.setText("EcoGraphics")
                celciustxt.setText("")
                changeTheme(6f,darkModeOrNot,true)
            }

            //Dersom det er godkjent
            override fun onResponse(call: Call<DataListSources>, response: Response<DataListSources>) {
                if(response.isSuccessful()){
                    val body = response?.body()
                    val dataSource = body?.dataSources

                    if (dataSource != null) {
                        for(data in dataSource!!) {
                            location_txt.text = data.name
                            sources = data.id
                            getTemperature()
                        }
                    } else {
                        //changeTheme(6f,darkModeOrNot,true)
                        locationtxt.setText("EcoGraphics")
                        celciustxt.setText("")
                        changeTheme(6f,darkModeOrNot,true)
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else Toast.makeText(this, "Permission denies", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                requestPermission()
                false
            }
        } else {
            true
        }
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE)
    }

    fun getTemperature() {
        println( "yay" + sources)
        /* ----TEMPERATUR TIL LOKASJON*/
        val optionsMap = HashMap<String,String>()
        optionsMap.put("sources",sources)
        optionsMap.put("referencetime","latest")
        optionsMap.put("elements","air_temperature")

        val service = RetrofitClientInstance.retrofitInstance?.create(
            GetObservationsService::class.java)
        val call = service?.getAllData(optionsMap)

        call?.enqueue(object: Callback<DataList> {
            val darkModeOrNot = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            //---->DERSOM DET FAILER
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                //Toast.makeText(applicationContext, "Error reading JSON! ${t}", Toast.LENGTH_LONG).show()
                //changeTheme(6f,darkModeOrNot,true)
            }
            //----> DERSOM DET SVAREER
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                val body = response?.body()
                val data = body?.data

                if (data != null) {
                    for (data in data) {
                        for (observation in data.observations) {

                            celcius.text = observation.value.toString() + "°"

                            changeTheme(observation.value,darkModeOrNot,false)
                        }
                    }
                } else {
                    changeTheme(6f,darkModeOrNot,true)
                }
            }
        })
    }

    fun changeTheme(temperature:Float,darkMode:Boolean,failure:Boolean) {
        val colorPrimaryBreezyLight = resources.getColor(R.color.purpure)
        val colorPrimaryWarmLight = resources.getColor(R.color.brown)
        val colorPrimaryColdLight = resources.getColor(R.color.winterblue)
        val colorPrimaryDark = resources.getColor(R.color.white)

        val backgroundImage = findViewById<ImageView>(R.id.background)
        val dato = findViewById<TextView>(R.id.dato)
        val location = findViewById<TextView>(R.id.location_txt)
        val txt_celcius = findViewById<TextView>(R.id.celcius)
        val dailyText = findViewById<TextView>(R.id.dagens)
        val dailyIcon = findViewById<ImageView>(R.id.dailyIcon)
        val futureText = findViewById<TextView>(R.id.future_text)
        val futureIcon = findViewById<ImageView>(R.id.future_icon)
        val mapText = findViewById<TextView>(R.id.map_text)
        val mapIcon = findViewById<ImageView>(R.id.map_icon)
        val monthlyText = findViewById<TextView>(R.id.monthly_text)
        val monthlyIcon = findViewById<ImageView>(R.id.monthly_icon)

        val dailyCard =    findViewById<CardView>(R.id.daily)
        val monthlyCard =  findViewById<CardView>(R.id.monthly)
        val futureCard =   findViewById<CardView>(R.id.future)
        val mapCard =      findViewById<CardView>(R.id.map)

        val mainPicture = findViewById<ImageView>(R.id.imageView)
        val outlineId = getStyledDrawableId(this, R.attr.mainPictureOutline)
        mainPicture.setImageDrawable(null)

        if(failure){
            txt_celcius.setText("")
            mainPicture.setImageResource(outlineId)
            if(darkMode){
                mainPicture.setColorFilter(colorPrimaryDark)
            }
            else {
                mainPicture.setColorFilter(colorPrimaryBreezyLight)
            }
        }

        mapIcon.setImageResource(R.drawable.ic_norway_flag)
        futureIcon.setImageResource(R.drawable.ic_chart)
        monthlyIcon.setImageResource(R.drawable.calendar_monthly)
        dailyIcon.setImageResource(R.drawable.ic_calendar)

        val texts = arrayListOf<TextView>(dato,location,txt_celcius,dailyText,futureText,mapText,monthlyText)
        val textsTop = arrayListOf<TextView>(dato,location)
        val icons = arrayListOf<ImageView>(dailyIcon,futureIcon,mapIcon,monthlyIcon)
        val cards = arrayListOf<CardView>(dailyCard,monthlyCard,futureCard,mapCard)

        var colorPrimary:Int

        val lowTempLimit = 5f
        val highTempLimit = 15f

        if(darkMode){
            colorPrimary = colorPrimaryDark
            for(card in cards){
                card.setCardBackgroundColor(Color.BLACK)
            }

            when {
                temperature in lowTempLimit..highTempLimit -> Glide.with(this)
                    .load(R.drawable.backgroundbreezydark)
                    .into(backgroundImage)
                    //backgroundImage.setImageResource(R.drawable.backgroundbreezydark)
                temperature > highTempLimit -> Glide.with(this)
                    .load(R.drawable.backgroundwarmdark)
                    .into(backgroundImage)
                    //backgroundImage.setImageResource(R.drawable.backgroundwarmdark)
                else -> Glide.with(this)
                    .load(R.drawable.background_colddark)
                    .into(backgroundImage)
                    //backgroundImage.setImageResource(R.drawable.background_colddark)
            }
        } else {
            when {
                temperature in lowTempLimit..highTempLimit -> {
                    colorPrimary = colorPrimaryBreezyLight
                    Glide.with(this)
                        .load(R.drawable.backgroundbreezy)
                        .into(backgroundImage)
                }
                temperature > highTempLimit -> {
                    colorPrimary = colorPrimaryWarmLight
                    Glide.with(this)
                        .load(R.drawable.backgroundwarm)
                        .into(backgroundImage)
                    //backgroundImage.setImageResource(R.drawable.backgroundwarm)
                }
                else -> {
                    //colorPrimaryColdLight
                    colorPrimary = Color.parseColor("#2e5ea1")
                    Glide.with(this)
                        .load(R.drawable.background_cold)
                        .into(backgroundImage)
                    //backgroundImage.setImageResource(R.drawable.background_cold)
                }
            }
        }

        for(text in texts){
            text.setTextColor(colorPrimary)
        }

        if(temperature < lowTempLimit && !darkMode){
            for(text in textsTop){
                text.setTextColor(colorPrimaryColdLight)
            }
        }

        for(icon in icons){
            icon.setColorFilter(colorPrimary,PorterDuff.Mode.SRC_IN)
        }
    }

    fun getStyledDrawableId(context: Context, attribute: Int): Int {
        val a = context.getTheme().obtainStyledAttributes(intArrayOf(attribute))
        return a.getResourceId(0, -1)
    }
}
