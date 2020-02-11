package com.example.ecographics.monthly

import android.app.Activity
import android.graphics.Color
import android.graphics.Color.rgb
import android.graphics.ColorFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import java.time.Year
import java.util.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import org.nield.kotlinstatistics.simpleRegression
import kotlin.collections.ArrayList
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
import android.graphics.DashPathEffect
import com.github.mikephil.charting.components.Legend
import kotlinx.android.synthetic.main.activity_fremtidsprognoser.view.*
import kotlinx.android.synthetic.main.fragment_fremtid_tmp.view.*
import kotlinx.android.synthetic.main.fragment_monthly_rain.view.*
import kotlinx.android.synthetic.main.fragment_monthly_tmp.view.*


class MonthlyDataGraph (val year: Float, val mChart: CombinedChart, val type: String, val min: Float, val max: Float,
                        val regresjon: String, val dataFraFrost: String, naa: String) {

    var size: Int = 0
    var x:DoubleArray = DoubleArray(size)
    var y: DoubleArray = DoubleArray(size)
    val limitLine: LimitLine = LimitLine(year.toFloat(), naa+": "+year.toInt() )

    fun createChart(maaned: List<Entry>, regression: List<Entry>){

        limitLine.textColor = rgb(246, 81, 96  )
        limitLine.lineWidth = 1f
        limitLine.textSize = 13f
        limitLine.textColor = Color.BLACK

        mChart.setTouchEnabled(true)
        mChart.setNoDataTextColor(Color.BLACK)
        mChart.xAxis.textSize = 13f
        mChart.xAxis.textColor= Color.BLACK
        mChart.setDrawGridBackground(true)
        mChart.setVisibleXRange(50F, 50F)
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setPinchZoom(true)
        mChart.xAxis.axisMinimum = 1915f

        mChart.xAxis.axisMaximum = 2050f
        mChart.description.text = type
        mChart.description.textSize = 13f
        mChart.fitScreen()

        val rightYAxis = mChart.getAxisRight()
        rightYAxis.isEnabled = false

        val leftYAxis:YAxis = mChart.axisLeft
        leftYAxis.textSize = 13f
        leftYAxis.textColor= Color.BLACK
        leftYAxis.labelCount = 5
        leftYAxis.axisMaximum = max
        leftYAxis.axisMinimum = min

        var xaxis: XAxis = mChart.xAxis
        xaxis.labelRotationAngle = 50f
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.addLimitLine(limitLine)

        mChart.description.textSize = 10f


        var dataCombined = CombinedData()
        dataCombined.setData(generateLineData(regression))
        dataCombined.setData(generateScatterData(maaned))

        val legend: Legend = mChart.legend
        legend.isEnabled = true
        legend.xEntrySpace = 15f
        legend.yEntrySpace = 0f
        legend.formToTextSpace = 5f
        legend.textSize = 13f


        mChart.data = dataCombined
        mChart.invalidate()
        mChart.refreshDrawableState()


    }

    private fun generateScatterData(maaned: List<Entry>): ScatterData {

        val d = ScatterData()
        val set = ScatterDataSet(maaned, dataFraFrost)

        set.setColors(rgb(246, 81, 96  ))
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE)
        set.formSize = 13f
        set.scatterShapeSize = 20f
        set.setDrawValues(false)
        set.setDrawHighlightIndicators(false)
        set.highlightLineWidth = 0f
        d.addDataSet(set)

        return d
    }

    private fun generateLineData(regression: List<Entry>): LineData {

        val d = LineData()
        val set = LineDataSet(regression,   regresjon)
        set.lineWidth = 2f
        set.setDrawCircleHole(false)
        set.formSize = 13f
        set.valueTextSize = 20f
        set.color = Color.DKGRAY
        set.setCircleColor(Color.DKGRAY)
        set.setDrawValues(false)
        set.setDrawCircles(false)

        set.axisDependency = YAxis.AxisDependency.LEFT

        d.addDataSet(set)


        return d
    }
}
