package com.example.ecographics.yearly

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ecographics.R
import com.example.ecographics.models.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class YearlyRainFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_fremtid_nedbor, null)
        val mChart: LineChart = rootView.findViewById(R.id.linechart_rain)
        val year: Float = Calendar.getInstance().get(Calendar.YEAR).toFloat()
        val graph: YearlyRainGraph =
            YearlyRainGraph(
                year,
                mChart,
                getString(R.string.nedborMM)
            )

        var amountYear: ArrayList<Entry> = arrayListOf(Entry())
        var amountSeason: ArrayList<Entry> = arrayListOf(Entry())

        var spinner = rootView.findViewById(R.id.dropdown) as Spinner
        val scenarios = arrayOf(getString(R.string.velgType), getString(R.string.nedborAar), getString(
            R.string.nedborSesong
        ))
        spinner.adapter = ArrayAdapter<String>(rootView.context, android.R.layout.simple_list_item_1, scenarios)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                graph.createChartRainY(emptyList())
                Toast.makeText(rootView.context, getString(R.string.velgType), Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (scenarios.get(position)) {
                    getString(R.string.velgType) -> graph.createChartRainY(emptyList())
                    getString(R.string.nedborAar) -> graph.createChartRainY(amountYear)
                    getString(R.string.nedborSesong) -> graph.createChartRainS(amountSeason)
                }
            }
        }

        val service = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
        val options = HashMap<String,String>()
        options.put("sources","GR0")
        options.put("referencetime","1900-01-01/2100-12-31")
        options.put("elements","sum(precipitation_amount P1Y)")
        val call = service?.getAllData(options)
        call?.enqueue(object: Callback<DataList> {
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                Toast.makeText(rootView.context, "Failed reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                //println(response?.body())
                val body = response.body()
                val data = body?.data

                for(data in data.orEmpty()){

                    for(observation in data.observations){
                        var reference: String = data.referenceTime.substring(0, 4)

                        //println(observation.value.toInt())

                        amountYear.add(Entry(reference.toFloat(), observation.value))
                    }
                }
            }
        })

        val service2 = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
        val optionsMap = HashMap<String,String>()
        optionsMap.put("sources","GR0")
        optionsMap.put("referencetime","1900-01-01/2100-12-31")
        optionsMap.put("elements","sum(precipitation_amount P3M)")

        val call2 = service2?.getAllData(optionsMap)
        call2?.enqueue(object: Callback<DataList> {
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                Toast.makeText(rootView.context, "Failed reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                //println(response?.body())
                val body = response.body()
                val data = body?.data
                var size = data!!.size

                for(data in data){

                    for(observation in data.observations){
                        var reference: String = data.referenceTime.substring(0, 4)
                        var month = data.referenceTime.substring(5, 7).toInt()
                        val one = 0.25
                        val two = 0.5
                        val three = 0.75
                        val four = 0.999999

                        //println(observation.value.toInt())

                        when(month) {
                            3 -> amountSeason.add(Entry(reference.toFloat() + one.toFloat(), observation.value))
                            6 -> amountSeason.add(Entry(reference.toFloat() + two.toFloat(), observation.value))
                            9 -> amountSeason.add(Entry(reference.toFloat() + three.toFloat(), observation.value))
                            12 -> amountSeason.add(Entry(reference.toFloat() + four.toFloat(), observation.value))
                        }

                        //average.add(Entry(reference.toFloat(), observation.value))
                    }
                }
            }
        })

        return rootView
    }

}