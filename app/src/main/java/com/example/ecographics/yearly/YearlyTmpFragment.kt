package com.example.ecographics.yearly

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ecographics.R
import com.example.ecographics.models.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class YearlyTmpFragment: Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_fremtid_tmp, null)
        val mChart: LineChart = rootView.findViewById(R.id.linechart_tmp)
        val year: Float = Calendar.getInstance().get(Calendar.YEAR).toFloat()
        val graph: YearlyTmpGraph = YearlyTmpGraph(
            year,
            mChart,
            getString(R.string.tmpCelcius)
        )


        var averageYear: ArrayList<Entry> = arrayListOf(Entry())
        var averageYearValue: Float = 0f
        var averageSeason: ArrayList<Entry> = arrayListOf(Entry())
        var averageSeasonValue: Float = 0f


        var spinner = rootView.findViewById(R.id.dropdown) as Spinner
        val scenarios = arrayOf(getString(R.string.velgType), getString(R.string.gjTmpAar), getString(
            R.string.gjTmpSesong
        ))
        spinner.adapter = ArrayAdapter<String>(rootView.context, android.R.layout.simple_list_item_1, scenarios)


        var yearOrSeason = "empty"
        var limitline:Boolean = false

        val switch = rootView.findViewById<Switch>(R.id.switch_year_tmp)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                limitline = true
                if(yearOrSeason=="year"){
                    graph.createChartYear(averageYear,true,averageYearValue)
                } else if(yearOrSeason=="season"){
                    graph.createChartSeason(averageSeason,true,averageSeasonValue)
                }
            } else {
                limitline = false
                when(yearOrSeason){
                    "year" -> graph.createChartYear(averageYear,false,0f)
                    "season" -> graph.createChartSeason(averageSeason,false,averageYearValue)
                }
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                graph.createChartYear(emptyList(),false,0f)
                Toast.makeText(rootView.context, getString(R.string.velgType), Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (scenarios.get(position)) {
                    getString(R.string.velgType) -> {
                        graph.createChartYear(emptyList(),limitline,0f)
                        yearOrSeason = "empty"
                    }
                    getString(R.string.gjTmpAar) -> {
                        graph.createChartYear(averageYear,limitline,averageYearValue)
                        yearOrSeason = "year"
                    }
                    getString(R.string.gjTmpSesong) -> {
                        graph.createChartSeason(averageSeason,limitline,averageSeasonValue)
                        yearOrSeason = "season"
                    }
                }
            }
        }


        val service = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
        val optionsMap = HashMap<String,String>()
        optionsMap.put("sources","GR0")
        optionsMap.put("referencetime","1900-01-01/2100-12-31")
        optionsMap.put("elements","mean(air_temperature P1Y)")
        val call = service?.getAllData(optionsMap)

        call?.enqueue(object: Callback<DataList> {
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                Toast.makeText(rootView.context, "Failed reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                //println(response?.body())
                val body = response.body()
                val data = body?.data
                var size = data!!.size
                var averageY = 0f
                var timesY = 0

                for(data in data){

                    for(observation in data.observations){
                        var reference: String = data.referenceTime.substring(0, 4)
                        averageY += observation.value
                        timesY += 1

                        averageYear.add(Entry(reference.toFloat(), observation.value))
                    }
                    averageYearValue = averageY/timesY
                }
            }
        })


        val service2 = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
        val options = HashMap<String,String>()
        options.put("sources","GR0")
        options.put("referencetime","1900-01-01/2100-12-31")
        options.put("elements","mean(air_temperature P3M)")
        val call2 = service2?.getAllData(options)
        call2?.enqueue(object: Callback<DataList> {
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                Toast.makeText(rootView.context, "Failed reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                //println(response?.body())
                val body = response.body()
                val data = body?.data
                var size = data!!.size
                var averageS = 0f
                var timesS = 0

                for(data in data){

                    for(observation in data.observations){
                        var reference: String = data.referenceTime.substring(0, 4)
                        var month = data.referenceTime.substring(5, 7).toInt()
                        val one = 0.25
                        val two = 0.5
                        val three = 0.75
                        val four = 0.999999

                        averageS += observation.value
                        timesS += 1

                        //println(observation.value.toInt())

                        when(month) {
                            3 -> averageSeason.add(Entry(reference.toFloat() + one.toFloat(), observation.value))
                            6 -> averageSeason.add(Entry(reference.toFloat() + two.toFloat(), observation.value))
                            9 -> averageSeason.add(Entry(reference.toFloat() + three.toFloat(), observation.value))
                            12 -> averageSeason.add(Entry(reference.toFloat() + four.toFloat(), observation.value))
                        }

                        //average.add(Entry(reference.toFloat(), observation.value))
                    }
                    averageSeasonValue = averageS/timesS
                }
            }
        })

        return rootView
    }

}