package com.example.ecographics

import com.github.mikephil.charting.data.Entry
import junit.framework.TestCase.assertEquals
import org.apache.commons.math3.stat.regression.RegressionResults
import org.apache.commons.math3.stat.regression.SimpleRegression
import org.junit.Test

class RegressionUnitTest {
    @Test
    fun regression_isCorrect(){

        val regression: SimpleRegression = SimpleRegression()
        regression.addData(1.toDouble(), 2.toDouble())
        regression.addData(2.toDouble(), 4.toDouble())
        regression.addData(3.toDouble(), 6.toDouble())
        regression.addData(4.toDouble(), 8.toDouble())


        assertEquals(2, regressionSlope(regression))
        assertEquals(0, regressionIntercept(regression))
    }

    fun regressionSlope(regression: SimpleRegression) : Int {

        val slope: Double = regression.slope
        return slope.toInt()

    }

    fun regressionIntercept(regression: SimpleRegression): Int{

        val intercept: Double = regression.intercept
        return intercept.toInt()

    }
}
