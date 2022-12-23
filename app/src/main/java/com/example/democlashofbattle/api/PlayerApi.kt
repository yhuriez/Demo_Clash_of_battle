package com.example.democlashofbattle.api

import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.utils.CapabilityMoshiConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface PlayerApi {

    companion object {

        private const val BASE_URL = "https://firechat-dev-da136-default-rtdb.europe-west1.firebasedatabase.app/"

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(CapabilityMoshiConverter())
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

        val service: PlayerApi by lazy { retrofit.create(PlayerApi::class.java) }
    }

    @GET("players.json")
    suspend fun getPlayers() : Map<String, Player>

    @PUT("players/{id}.json")
    suspend fun updatePlayer(@Path("id") id: String, @Body player: Player) : Player
}