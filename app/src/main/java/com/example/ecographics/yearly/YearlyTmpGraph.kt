package com.example.ecographics.yearly

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet



class YearlyTmpGraph (val year: Float, val mChart: LineChart, val type: String){

    fun createChartYear(scenarios: List<Entry>, limitline: Boolean, averageValue: Float){
        mChart.axisLeft.removeAllLimitLines()
        mChart.axisRight.isEnabled = false
        mChart.setTouchEnabled(true)
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setPinchZoom(true)

        mChart.axisLeft.axisMinimum = -2f
        mChart.axisLeft.axisMaximum = 4f
        mChart.xAxis.axisMinimum = 1900f
        mChart.xAxis.axisMaximum = 2019f
        mChart.setVisibleXRange(40F, 40F)

        mChart.xAxis.textSize = 11f
        mChart.axisLeft.textSize = 11f
        mChart.description.text = type
        mChart.description.textSize = 11f


        var set1 = LineDataSet(scenarios, "")
        var dataSets : ArrayList<ILineDataSet> = arrayListOf()
        dataSets.add(set1)
        set1.valueTextSize = 10f

        var data = LineData(dataSets)
        mChart.data = data

        set1.setColor(Color.BLACK, 100)
        set1.highLightColor = Color.BLUE
        set1.highlightLineWidth = 2f

        if(limitline){
            val limit = LimitLine(averageValue, "Average")
            limit.lineWidth = 3f
            limit.enableDashedLine(3f, 3f, 0f)
            limit.textSize = 13f
            mChart.axisLeft.addLimitLine(limit)
        }
        mChart.invalidate()
    }


    fun createChartSeason(scenarios: List<Entry>,limitline:Boolean,averageValue:Float){
        mChart.axisLeft.removeAllLimitLines()
        mChart.axisRight.isEnabled = false
        mChart.setTouchEnabled(true)
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setPinchZoom(true)

        mChart.axisLeft.axisMinimum = -15f
        mChart.axisLeft.axisMaximum = 15f
        mChart.xAxis.axisMinimum = 1900f
        mChart.xAxis.axisMaximum = 2019f
        mChart.setVisibleXRange(10F, 10F)

        mChart.xAxis.textSize = 11f
        mChart.axisLeft.textSize = 11f
        mChart.description.text = type
        mChart.description.textSize = 11f


        var set1 = LineDataSet(scenarios, "")
        var dataSets : ArrayList<ILineDataSet> = arrayListOf()
        dataSets.add(set1)
        set1.valueTextSize = 10f

        var data = LineData(dataSets)
        mChart.data = data

        set1.setColor(Color.BLACK, 100)
        set1.highLightColor = Color.BLUE
        set1.highlightLineWidth = 2f

        if(limitline){
            val limit = LimitLine(averageValue, "Average")
            limit.lineWidth = 3f
            limit.enableDashedLine(3f, 3f, 0f)
            limit.textSize = 13f

            mChart.axisLeft.addLimitLine(limit)
        }
        mChart.invalidate()
    }

}