package com.adiupd123.fingertrip.api

import com.adiupd123.fingertrip.models.OpenSeaAsset
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NFTApi {
    @GET("/listings/")
    suspend fun getAssets(
    @Query("owner") ownerAddress: String,
    @Query("order_direction") orderDirection: String,
    @Query("offset") offset: Int,
    @Query("limit") limit: Int,
    @Header("X-API-KEY") apiKey: String
    ): Call<List<OpenSeaAsset>>
}