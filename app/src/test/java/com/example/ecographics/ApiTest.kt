package com.example.ecographics

import android.support.v7.app.AppCompatDelegate
import android.widget.TextView
import com.example.ecographics.models.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_dagens_temperatur.*
import okhttp3.*
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.io.IOException

class ApiTest {
    @Test
    fun main() {
        val optionsMap = HashMap<String,String>()
        optionsMap.put("sources","SN18700")
        optionsMap.put("referencetime","latest")
        optionsMap.put("elements","air_temperature")

        val service = RetrofitClientInstance.retrofitInstance?.create(
            GetObservationsService::class.java)
        val call = service?.getAllData(optionsMap)

        call?.enqueue(object: Callback<DataList> {
            //---->DERSOM DET FAILER
            override fun onFailure(call: Call<DataList>, t: Throwable) {
                println("failure")
            }
            //----> DERSOM DET SVAREER
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                val body = response?.body()
                println(body)
            }
        })
    }
}