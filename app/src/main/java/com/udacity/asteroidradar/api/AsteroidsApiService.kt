package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class AsteroidsApiService {

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object pointing to the desired URL
     */


    /**
     * A public interface that exposes the [getAsteroids] method
     */
    interface AsteroidsApiService {
        @GET("neo/rest/v1/feed")
        suspend fun getAsteroids(
            @Query("api_key") api_key: String
        ): String

        @GET("planetary/apod")
        suspend fun getPictureOfTheDay(
            @Query("api_key") api_key: String
        ): PictureOfDay
    }

    object AsteroidsApi {

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()



        val retrofitService : AsteroidsApiService by lazy { retrofit.create(AsteroidsApiService::class.java) }
    }
}