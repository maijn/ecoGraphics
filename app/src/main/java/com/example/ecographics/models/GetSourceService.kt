package com.example.ecographics.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GetSourceService {
    @GET("sources/v0.jsonld")
    fun getAllData(@QueryMap options:HashMap<String,String>): Call<DataListSources>
}