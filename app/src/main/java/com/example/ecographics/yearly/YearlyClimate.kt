package com.example.ecographics.yearly

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem
import com.example.ecographics.daily.DailyTemperature
import com.example.ecographics.R


class YearlyClimate : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

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
            R.id.temperatur -> fragment = YearlyTmpFragment()
            R.id.nedbør -> fragment = YearlyRainFragment()
        }
        return loadFragment(fragment)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        //DARK MODE CHECK
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        }
        else {
            setTheme(R.style.lightThemeFuture)
        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fremtidsprognoser)

        //val actionbar = supportActionBar
        //actionbar!!.title = resources.getString(R.string.fremtidsprognoser)
        //actionbar.setDisplayHomeAsUpEnabled(true)

        val home_button = findViewById<FloatingActionButton>(R.id.fab)
        home_button.setOnClickListener {
            val intent = Intent(this, DailyTemperature::class.java)
            startActivity(intent)
        }

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        navigation.setOnNavigationItemSelectedListener(this)

        loadFragment(YearlyTmpFragment())

    }

    /*private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    var scenario1: ArrayList<Entry> = arrayListOf(Entry())
    //var scenario2: ArrayList<Entry> = arrayListOf(Entry())
    //var scenario3: ArrayList<Entry> = arrayListOf(Entry())
    //var scenario4: ArrayList<Entry> = arrayListOf(Entry())


    fun createChart(scenario: List<Entry>){
        var mChart: LineChart = findViewById(R.id.linechart)
        mChart.setOnChartValueSelectedListener(this)
        mChart.axisLeft.axisMinimum = -2f
        mChart.axisLeft.axisMaximum = 4f
        mChart.axisRight.axisMinimum = -2f
        mChart.axisRight.axisMaximum = 4f
        mChart.xAxis.axisMinimum = 1900f
        mChart.xAxis.axisMaximum = 2100f
        mChart.setVisibleXRange(50F, 50F)

        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setPinchZoom(true)

        var set1 = LineDataSet(scenario, "")
        var dataSets : ArrayList<ILineDataSet> = arrayListOf()
        dataSets.add(set1)

        var data = LineData(dataSets)
        mChart.data = data

        set1.setColor(Color.BLACK, 100)
        set1.highLightColor = Color.BLUE
        set1.highlightLineWidth = 3f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fremtidsprognoser)

        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val spinner = findViewById<Spinner>(R.id.velgScenario)
        val scenarios = arrayOf("Scenario 1") //"Scenario 2", "Scenario 3", "Scenario 4"
        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scenarios)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (scenarios.get(position)) {
                    "Scenario 1" -> createChart(scenario1)
                    //"Scenario 2" -> createChart(scenario2)
                    //"Scenario 3" -> createChart(scenario3)
                    //"Scenario 4" -> createChart(scenario4)
                }
            }
        }

        val service = RetrofitClientInstance.retrofitInstance?.create(GetDataFuture::class.java)
        val call = service?.getAllDataFP()
        call?.enqueue(object: Callback<DataListPrognose>{
            override fun onFailure(call: Call<DataListPrognose>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DataListPrognose>, response: Response<DataListPrognose>) {
                //println(response?.body())
                val body = response?.body()
                val data = body?.data
                var size = data!!.size
                //var totalAverage = 0

                for(data in data){

                    for(observation in data.observations){
                        var reference: String = data.referenceTime.substring(0, 4)
                        //totalAverage += observation.value.toInt()
                        println(observation.value.toInt())

                        scenario1.add(Entry(reference.toFloat(), observation.value))
                        //scenario2.add(Entry(reference.toFloat(), observation.value))
                        //scenario3.add(Entry(reference.toFloat(), observation.value))
                        //scenario4.add(Entry(reference.toFloat(), observation.value))
                    }
                }
                //println((totalAverage/(2019-1900)).toFloat())
            }
        })
    }*/
}
