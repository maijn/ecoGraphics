package com.example.ecographics.monthly

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ecographics.Month
import com.example.ecographics.models.DataList
import com.example.ecographics.R
import com.example.ecographics.models.GetObservationsService
import com.example.ecographics.models.RetrofitClientInstance
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.fragment_monthly_rain.*
import kotlinx.android.synthetic.main.fragment_monthly_tmp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MonthlyRainFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //super.onCreate(savedInstanceState)
        val rootView =  inflater.inflate(R.layout.fragment_monthly_rain, null)
        val mChart: CombinedChart = rootView.findViewById(R.id.combinedChartRain)
        val year: Float = Calendar.getInstance().get(Calendar.YEAR).toFloat()
        val graph: MonthlyDataGraph = MonthlyDataGraph(
            year,
            mChart,
            getString(R.string.nedborMM),
            0f,
            300f,
            getString(R.string.regresjon),
            getString(R.string.dataFraFrost),
            getString(R.string.naa)
        )
        //val months: Months = Months()
        val months: HashMap<Int, Month> = hashMapOf(0 to Month(name = getString(R.string.noInfo)), 1 to Month(name = getString(R.string.januar)), 2 to Month(name = getString(R.string.februar)),
            3 to Month(name = getString(R.string.mars)), 4 to Month(name = getString(R.string.april)), 5 to Month(name = getString(R.string.mai)),
            6 to Month(name = getString(R.string.juni)), 7 to Month(name = getString(R.string.juli)), 8 to Month(name = getString(R.string.august)),
            9 to Month(name = getString(R.string.september)), 10 to Month(name = getString(R.string.oktober)), 11 to Month(name = getString(R.string.november)),
            12 to Month(name = getString(R.string.desember))
        )
        graph.createChart(emptyList(), emptyList())

        //val scroller: ScrollView = rootView.findViewById(R.id.scrollRain)
        //val infoText: TextView = rootView.findViewById(R.id.textViewRain)


        var valg = rootView.findViewById(R.id.valgRain) as Spinner
        val maaneder = arrayOf(
            getString(R.string.velgMaaned), getString(R.string.januar), getString(
                R.string.februar
            ), getString(R.string.mars),
            getString(R.string.april), getString(R.string.mai), getString(
                R.string.juni
            ), getString(R.string.juli), getString(R.string.august),
            getString(R.string.september), getString(R.string.oktober), getString(
                R.string.november
            ), getString(R.string.desember)
        )

        valg.adapter = ArrayAdapter<String>(rootView.context, android.R.layout.simple_list_item_1, maaneder)

        ///!!!


            val service = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
            val monthlyRainOptions = HashMap<String,String>()
            monthlyRainOptions.put("sources","GR0")
            monthlyRainOptions.put("referencetime","1920-01-01/2040-01-01")
            monthlyRainOptions.put("elements","sum(precipitation_amount P1M)")
            val call = service?.getAllData(monthlyRainOptions)
            call?.enqueue(object : Callback<DataList> {
                override fun onFailure(call: Call<DataList>, t: Throwable) {

                    Toast.makeText(rootView.context, "Error reading JSON! ${t}", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                    //graph.createChart(emptyList(), emptyList())

                    val body = response.body()
                    val data = body?.data
                    var size = data!!.size

                    for(data in data){
                        for(observation in data.observations){
                            var reference: String = data.referenceTime.substring(0, 4)
                            var maaned = data.referenceTime.substring(5,7).toInt()

                            months.get(maaned)!!.addData(reference.toInt(), observation.value.toInt())


                        }
                    }
                    //må ha den her, hvis ikke så vil ikke dataene loade
                    valg.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            graph.createChart(emptyList(), emptyList())
                            Toast.makeText(rootView.context, getString(R.string.velgMaaned), Toast.LENGTH_LONG).show()
                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                           var change: String = ""
                            try{

                                if(position != 0) {
                                    graph.createChart(
                                        months.get(position)!!.data,
                                        months.get(position)!!.regression(getString(R.string.nedbor), textViewRain)
                                    )

                                    if (months.get(position)!!.getSlope() > 0.toString()) {
                                        change = getString(R.string.okt)
                                    } else {
                                        change = getString(
                                            R.string.minket
                                        )
                                    }
                                    val info: String = getString(
                                        R.string.infoTekst,
                                        months.get(position)!!.name,
                                        getString(R.string.nedbør),
                                        change,
                                        months.get(position)!!.getSlope(),
                                        getString(
                                            R.string.mm
                                        )
                                    )

                                    textViewRain.setText(Html.fromHtml(info), TextView.BufferType.SPANNABLE)

                                } else{
                                    graph.createChart(emptyList(), emptyList())
                                    textViewRain.setText(getText(R.string.noInfo))
                                }

                            } catch(e: org.apache.commons.math3.exception.NoDataException){
                                println(e)
                            }
                        }
                    }


                }
            })

            return rootView
    }
}