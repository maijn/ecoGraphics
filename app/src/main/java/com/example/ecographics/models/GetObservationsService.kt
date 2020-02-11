package com.example.ecographics.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GetObservationsService {
    @GET("observations/v0.jsonld")
    fun getAllData(@QueryMap options:Map<String,String>): Call<DataList>
}