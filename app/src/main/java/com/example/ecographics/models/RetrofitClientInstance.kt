package com.example.ecographics.models

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//https://frost.met.no/observations/v0.jsonld?sources=GR0&referencetime=1920-01-01%2F2020-01-01&elements=mean(air_temperature%20P1M)
//https://frost.met.no/observations/v0.jsonld?sources=GR0&referencetime=1920-01-01%2F2100-12-31&elements=mean(air_temperature%20P1M)
object  RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private var BASE_URL: String = "https://in2000-frostproxy.ifi.uio.no/"

    val retrofitInstance:Retrofit?

        get(){
        if(retrofit == null){
            retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }



/*
    @FormUrlEncoded
    @POST("/oauth/token")
     fun getNewAccessToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String
    ) {
    }

    @FormUrlEncoded
    @POST("/oauth/token")
     fun getRefreshAccessToken(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String
    ) {
    }
    */
}