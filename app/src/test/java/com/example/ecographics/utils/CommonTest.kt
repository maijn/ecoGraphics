package com.example.ecographics.utils

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CommonTest {

    @Test
    fun getStyledDrawableId() {
        var context = InstrumentationRegistry.getInstrumentation().context
        val common = Common()
        assert(common.getStyledDrawableId(context,0)==-1)
    }
}