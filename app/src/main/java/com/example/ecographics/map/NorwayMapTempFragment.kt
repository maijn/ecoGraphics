package com.example.ecographics.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.ecographics.R
import com.example.ecographics.models.*
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NorwayMapTempFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_norgeskart_temp, null)

        // FYLKENE
        val singlePathFylker = arrayOf("Østfold","Akershus","Oslo","Hedmark","Oppland","Buskerud",
            "Vestfold","Telemark","Aust-agder","Vest-agder","Rogaland","Nord-trøndelag")

        val groupPathFylker = arrayOf("Hordaland","Sogn-og-fjordane","Møre-og-romsdal","Sør-trøndelag","Nordland",
            "Troms","Finnmark")

        // Fargetema
        val colors = arrayOf("#00FFFF","#00F0FF","#00D6FF","#00B7FF", "#24A7FD", "#4694E9", "#6F7DCF", "#876DBF",
            "#9566B4", "#B653A0", "#DF3A86", "#EC3A86", "#DE2B55","#F6171E","#E01700","#C61700","#000000")

        val colorblindColors = arrayOf("#FCDD7D","#F4D87D","#E7D17C","#DECD7B","#D3C87A","#BCBC78","#B1B678","#9EAB76",
            "#8BA275","#7C9A73","#6B9271","#5B8B70","#4F8371","#4F8371","#37776F","#2E736F","#000000")

        val norgeVector = rootView.findViewById(R.id.norgeskart_vector_temp) as VectorMasterView
        val seekBar = rootView.findViewById<SeekBar>(R.id.seekBar_temp)
        val switch = rootView.findViewById<Switch>(R.id.fargeblind)
        val legend = rootView.findViewById<ImageView>(R.id.norgeskart_legend_temp)

        var colorsChosen:Array<String> = colors
        //SWITCH

        // Lage fylkeobjektene
        val fylkeObjekter = HashMap<String, County>()
        for(fylke in singlePathFylker){
            if(fylke !in fylkeObjekter){
                fylkeObjekter.put(fylke, County(fylke, "single", norgeVector))
            }
        }
        for(fylke in groupPathFylker){
            if(fylke !in fylkeObjekter){
                fylkeObjekter.put(fylke, County(fylke, "group", norgeVector))
            }
        }

        //API-kall for vanlige fylker
        val service = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
        val kartOptions = HashMap<String,String>()
        kartOptions.put("sources","SN17150,SN17850,SN18700,SN7010," +
                "SN11500,SN27500,SN39040,SN35820,SN47300," +
                "SN48330,SN60500,SN80700,SN90450,SN93900")
        kartOptions.put("referencetime","1950/2019")
        kartOptions.put("elements","mean(air_temperature P1Y)")
        val call = service?.getAllData(kartOptions)

        call?.enqueue(object : Callback<DataList> {
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                Toast.makeText(rootView.context, "Error reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {

                val body = response?.body()
                val data = body?.data

                for(data in data.orEmpty()){
                    for(observation in data.observations){
                        var source: String = data.sourceId
                        var year: Int = data.referenceTime.substring(0, 4).toInt()
                        var temperature: Float = observation.value
                        when(source){
                            "SN18700:0" -> fylkeObjekter["Oslo"]!!.addTemp(year,temperature)
                            "SN11500:0" -> fylkeObjekter["Oppland"]!!.addTemp(year,temperature)
                            "SN7010:0" ->  fylkeObjekter["Hedmark"]!!.addTemp(year,temperature)
                            "SN80700:0" -> fylkeObjekter["Nordland"]!!.addTemp(year,temperature)
                            "SN93900:0" -> fylkeObjekter["Finnmark"]!!.addTemp(year,temperature)
                            "SN17150:0" -> fylkeObjekter["Østfold"]!!.addTemp(year,temperature)
                            "SN17850:0" -> fylkeObjekter["Akershus"]!!.addTemp(year,temperature)
                            "SN47300:0" -> fylkeObjekter["Rogaland"]!!.addTemp(year,temperature)
                            "SN27500:0" -> fylkeObjekter["Vestfold"]!!.addTemp(year,temperature)
                            "SN39040:0" -> fylkeObjekter["Vest-agder"]!!.addTemp(year,temperature)
                            "SN48330:0" -> fylkeObjekter["Hordaland"]!!.addTemp(year,temperature)
                            "SN60500:0" -> fylkeObjekter["Møre-og-romsdal"]!!.addTemp(year,temperature)
                            "SN90450:0" -> fylkeObjekter["Troms"]!!.addTemp(year,temperature)
                        }
                    }
                }
                for(fylke in fylkeObjekter.values){
                    fylke.changeColor(1966,"temp",colorsChosen)
                    norgeVector.update()
                }
            }

        })

        // API-kall for GR-fylkene
        val serviceGR = RetrofitClientInstance.retrofitInstance?.create(GetObservationsService::class.java)
        val grOptions = HashMap<String,String>()
        grOptions.put("sources","GR1,GR2,GR3,GR4")
        grOptions.put("referencetime","1950/2019")
        grOptions.put("elements","mean(air_temperature P1Y)")
        val callGR = serviceGR?.getAllData(grOptions)

        callGR?.enqueue(object : Callback<DataList> {
            override fun onFailure(callGR: Call<DataList>, t: Throwable) {
                Toast.makeText(rootView.context, "Error reading JSON! ${t}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(callGR: Call<DataList>, response: Response<DataList>) {

                val body = response?.body()
                val data = body?.data

                for(data in data.orEmpty()){
                    for(observation in data.observations){
                        val source: String = data.sourceId
                        val year: Int = data.referenceTime.substring(0, 4).toInt()
                        val temperature: Float = observation.value
                        when(source){
                            "GR1" -> {
                                fylkeObjekter["Telemark"]!!.addTemp(year,temperature)
                                fylkeObjekter["Buskerud"]!!.addTemp(year,temperature)
                            }
                            "GR2" -> fylkeObjekter["Aust-agder"]!!.addTemp(year,temperature)
                            "GR3" -> fylkeObjekter["Sogn-og-fjordane"]!!.addTemp(year,temperature)
                            "GR4" -> {
                                fylkeObjekter["Nord-trøndelag"]!!.addTemp(year,temperature)
                                fylkeObjekter["Sør-trøndelag"]!!.addTemp(year,temperature)
                            }
                        }
                    }
                }
                for(fylke in fylkeObjekter.values){
                    fylke.changeColor(1966,"temp",colorsChosen)
                    norgeVector.update()
                }
            }
        })

        // SEEK


        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                colorsChosen=colorblindColors
                Glide.with(this)
                    .load(R.drawable.legend_temp_colorblind)
                    .into(legend)
                //legend.setImageResource(R.drawable.legend_temp_colorblind)
            } else {
                colorsChosen=colors
                Glide.with(this)
                    .load(R.drawable.legend_temp_bluepurple)
                    .into(legend)
                //legend.setImageResource(R.drawable.legend_temp_bluepurple)
            }

            for(fylke in fylkeObjekter.values){
                fylke.changeColor(1966,"temp",colorsChosen)
                norgeVector.update()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var arstallTekst = rootView.findViewById<TextView>(R.id.arstallTekst_temp)
                var currentYear = (progress*2)+1960
                var yearText = ""+currentYear
                arstallTekst.setText(yearText)

                for(fylke in fylkeObjekter.values){
                    fylke.changeColor(currentYear,"temp",colorsChosen)
                }
                norgeVector.update()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        return rootView
    }
}