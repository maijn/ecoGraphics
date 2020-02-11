package com.example.ecographics.map
import android.content.Context
import org.junit.Test
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountyTest {
    lateinit var context: Context

    @Test
    fun getName() {
        context = InstrumentationRegistry.getInstrumentation().context
        assert(County("test","", VectorMasterView(context)).name=="test")
    }

    @Test
    fun addTemp() {
        context = InstrumentationRegistry.getInstrumentation().context
        val test = County("test","single",VectorMasterView(context))
        test.addTemp(2000,20.5f)
        assert(test.tempMap[2000]==20.5f)
    }

    @Test
    fun addRegn() {
        context = InstrumentationRegistry.getInstrumentation().context
        val test = County("test","single",VectorMasterView(context))
        test.addRegn(2000,20.5f)
        assert(test.regnMap[2000]==20.5f)
    }

    @Test
    fun changeColor() {
        context = InstrumentationRegistry.getInstrumentation().context
        val vector = VectorMasterView(context)
        val test = County("akershus","test",vector)
        test.addTemp(2000,10f)
        test.changeColor(2000,"temp")
    }

    @Test
    fun changeColorRain() {
        context = InstrumentationRegistry.getInstrumentation().context
        val vector = VectorMasterView(context)
        val test = County("akershus","test",vector)
        test.addRegn(2000,1000f)
        test.changeColorRain(2000)
    }
}