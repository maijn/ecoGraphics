package com.example.ecographics

import android.widget.TextView
import com.github.mikephil.charting.data.Entry
import org.apache.commons.math3.stat.regression.SimpleRegression


class Month(var data: ArrayList<Entry> = arrayListOf(), var regression: SimpleRegression = SimpleRegression(), var regressionData: ArrayList<Entry> = arrayListOf(), val name: String){
   // val makeMonthlyInfoText: MakeMonthlyInfoText = MakeMonthlyInfoText(slope, type)

    fun addData(reference: Int, value: Int){
        regression.addData(reference.toDouble(), value.toDouble())
        data.add(Entry(reference.toFloat(), value.toFloat()))

    }

    fun getSlope(): String{
        return "%.3f".format(regression.slope)
    }

    fun regression(type: String, infoTextView: TextView) : List<Entry>{

        val slope: Double = regression.slope
        val intercept: Double = regression.intercept



        for (i in 1920..2050) {
            regressionData.add(
                Entry(
                    (i).toFloat(),
                    ((((i * slope) + intercept))).toFloat()
                )
            )


        }

        //graph.createChart(data, regressionData)
       // val makeMonthlyInfoText: MakeMonthlyInfoText = MakeMonthlyInfoText()
        //var info = makeMonthlyInfoText.makeText(slope, type, "Month")

        //infoTextView.text = info
        //infoTextView.setText(Html.fromHtml(info), TextView.BufferType.SPANNABLE)




        return regressionData

    }
}