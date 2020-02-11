package com.example.ecographics.yearly

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class YearlyRainGraph(val year: Float, val mChart: LineChart, val type: String) {

    fun createChartRainY(scenarios: List<Entry>){

        mChart.axisRight.isEnabled = false
        mChart.setTouchEnabled(true)
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setPinchZoom(true)

        mChart.axisLeft.axisMinimum = 0f
        mChart.axisLeft.axisMaximum = 1600f
        mChart.axisRight.axisMinimum = 0f
        mChart.axisRight.axisMaximum = 1600f
        mChart.xAxis.axisMinimum = 1900f
        mChart.xAxis.axisMaximum = 2019f
        mChart.setVisibleXRange(30F, 30F)

        mChart.xAxis.textSize = 11f
        mChart.axisLeft.textSize = 11f
        mChart.description.text = type
        mChart.description.textSize = 11f


        var set1 = LineDataSet(scenarios, "")
        var dataSets : ArrayList<ILineDataSet> = arrayListOf()
        dataSets.add(set1)
        set1.valueTextSize = 10f
        set1.setDrawFilled(true)


        var data = LineData(dataSets)
        mChart.data = data

        set1.setColor(Color.BLACK, 100)
        set1.highLightColor = Color.BLUE
        set1.highlightLineWidth = 2f

        mChart.invalidate()
    }

    fun createChartRainS(scenarios: List<Entry>){

        mChart.axisRight.isEnabled = false
        mChart.setTouchEnabled(true)
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setPinchZoom(true)

        mChart.axisLeft.axisMinimum = 0f
        mChart.axisLeft.axisMaximum = 500f
        mChart.axisRight.axisMinimum = 0f
        mChart.axisRight.axisMaximum = 500f
        mChart.xAxis.axisMinimum = 1900f
        mChart.xAxis.axisMaximum = 2019f
        mChart.setVisibleXRange(20F, 20F)

        mChart.xAxis.textSize = 11f
        mChart.axisLeft.textSize = 11f
        mChart.description.text = type
        mChart.description.textSize = 11f


        var set1 = LineDataSet(scenarios, "")
        var dataSets : ArrayList<ILineDataSet> = arrayListOf()
        dataSets.add(set1)
        set1.valueTextSize = 10f
        set1.setDrawFilled(true)

        var data = LineData(dataSets)
        mChart.data = data

        set1.setColor(Color.BLACK, 100)
        set1.highLightColor = Color.BLUE
        set1.highlightLineWidth = 2f

        mChart.invalidate()
    }
}