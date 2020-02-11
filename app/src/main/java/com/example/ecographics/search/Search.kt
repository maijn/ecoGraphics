package com.example.ecographics.search

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import android.widget.AdapterView
import android.widget.TextView
import com.example.ecographics.R
import com.example.ecographics.daily.DailyTemperature
import com.example.ecographics.models.DataList
import com.example.ecographics.models.GetObservationsService
import com.example.ecographics.models.RetrofitClientInstance


class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //DARK MODE CHECK
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkThemeSearch)
        }
        else {
            setTheme(R.style.lightThemeSearch)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //XML-ELEMENTER
        val button = findViewById<Button>(R.id.submitButton)
        val spinner = findViewById<Spinner>(R.id.station_spinner)
        val spinnerElement = findViewById<Spinner>(R.id.element_spinner)
        val sun = findViewById<ImageView>(R.id.sun_search)
        val sunId = getStyledDrawableId(this, R.attr.searchPicture)
        sun.setImageResource(sunId)
        val datePicker = findViewById<EditText>(R.id.datePicker)

        // KALENDERE
        var cal = Calendar.getInstance()
        val today = Calendar.getInstance().timeInMillis
        val format = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(format, Locale.US)
        datePicker.setText(formatter.format(today))

        // Fikser spinnerfarge for dark mode...
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            spinner.setSelection(0, true)
            val v = spinner.selectedView
            (v as TextView).setTextColor(Color.WHITE)
            spinnerElement.setSelection(0, true)
            val vi = spinnerElement.selectedView
            (vi as TextView).setTextColor(Color.WHITE)

            //Set the listener for when each option is clicked.
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //Change the selected item's text color
                    (view as TextView).setTextColor(Color.WHITE)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            spinnerElement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    //Change the selected item's text color
                    (view as TextView).setTextColor(Color.WHITE)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            datePicker.setText(sdf.format(cal.time))
        }

        datePicker.setOnClickListener {
            val dialog = DatePickerDialog(
                this, dateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dialog.show()
        }

        val home_button = findViewById<FloatingActionButton>(R.id.fab)
        home_button.setOnClickListener {
            val intent = Intent(this, DailyTemperature::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            var date = datePicker.text.toString()
            var station = spinner.selectedItem as String
            var elementSelected = spinnerElement.selectedItem as String
            var stationCode = ""
            var elementCode = ""
            when(station){
                "Oslo (Blindern)"           -> stationCode = "SN18700"
                "Ås (NMBU) - Akershus"                 -> stationCode = "SN17850"
                "Rygge - Østfold"           -> stationCode = "SN17150"
                "Østre Toten - Oppland"     -> stationCode = "SN11500"
                "Færder - Vestfold"         -> stationCode = "SN27500"
                "Kristiansand - Vest-Agder" -> stationCode = "SN39040"
                "Utsira - Rogaland"         -> stationCode = "SN47300"
                "Bømlo - Hordaland"         -> stationCode = "SN48330"
                "Norddal - Møre og Romsdal" -> stationCode = "SN60500"
                "Meløy - Nordland"          -> stationCode = "SN80700"
                "Tromsø - Troms"            -> stationCode = "SN90450"
                "Kautokeino - Finnmark"     -> stationCode = "SN93900"
                "Hornsund - Svalbard"       -> stationCode = "SN99750"
                "Jan Mayen"                 -> stationCode = "SN99950"
                "Hele Norge"                -> stationCode = "GR0"
            }
            when(elementSelected){
                "Temperature" -> elementCode = "mean(air_temperature P1D)"
                "Rainfall"    -> elementCode = "sum(precipitation_amount P1D)"
                "Snowfall"    -> elementCode = "surface_snow_thickness"
                "Temperatur" -> elementCode = "mean(air_temperature P1D)"
                "Nedbør"    -> elementCode = "sum(precipitation_amount P1D)"
                "Snømengde"    -> elementCode = "surface_snow_thickness"
                "气温数据" -> elementCode = "mean(air_temperature P1D)"
                "雨量数据"    -> elementCode = "sum(precipitation_amount P1D)"
                "雪量数据"    -> elementCode = "surface_snow_thickness"
            }

            val resultText = findViewById<TextView>(R.id.result_date)
            getInformation(stationCode,elementCode,elementSelected,date,station,
                "Norway",resultText)
        }

    }

    fun getStyledDrawableId(context: Context, attribute: Int): Int {
        val a = context.getTheme().obtainStyledAttributes(intArrayOf(attribute))
        return a.getResourceId(0, -1)
    }

    fun getInformation(sourceId:String,element:String,elementName:String,
                       date:String,name:String,country:String,resultText:TextView) {
        val optionsMap = HashMap<String,String>()
        optionsMap.put("sources",sourceId)
        optionsMap.put("elements",element)
        optionsMap.put("referencetime",date)

        val resultDate = findViewById<TextView>(R.id.result_date)
        val resultValue = findViewById<TextView>(R.id.result_value)
        val resultStation = findViewById<TextView>(R.id.result_station)
        val resultDatatype = findViewById<TextView>(R.id.datatype)
        val noResult = findViewById<TextView>(R.id.nodata_text)
        val sunImage = findViewById<ImageView>(R.id.sun_search)

        val sunId = getStyledDrawableId(this, R.attr.searchPicture)

        val service = RetrofitClientInstance.retrofitInstance?.create(
            GetObservationsService::class.java
        )
        val call = service?.getAllData(optionsMap)

        call?.enqueue(object : Callback<DataList> {
            //---->DERSOM DET FAILER
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                println("failure..sad")
            }

            //----> DERSOM DET SVAREER
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {

                val body = response?.body()
                val data = body?.data
                println(data)

                if (data != null) {
                    for (data in data) {
                        for (observation in data.observations) {
                            println("value is "+observation.value)
                            sunImage.setImageDrawable(null)
                            noResult.setText("")

                            resultDate.setText(date)
                            resultValue.setText(observation.value.toString()+" "+observation.unit.toString())
                            resultDatatype.setText(elementName)

                            resultStation.setText(name)

                            /*resultText.setText(name+", "+observation.value.toString()+
                                    ", "+observation.unit.toString())*/
                        }
                    }
                } else {
                    resultDate.setText("")
                    resultValue.setText("")
                    resultDatatype.setText("")
                    resultStation.setText("")
                    sunImage.setImageResource(sunId)
                    //R.attr.searchPicture
                    noResult.setText(getString(R.string.noData,name,elementName.toLowerCase()))
                }
            }
        })
    }

    fun hideKeyboard (view:View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
