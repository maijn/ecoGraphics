package com.example.ecographics.models

data class Observations(val elementId: String, val value: Float, val unit: String, val timeOffset: String, val timeResolution: String,
                        val timeSeriesId: Int)