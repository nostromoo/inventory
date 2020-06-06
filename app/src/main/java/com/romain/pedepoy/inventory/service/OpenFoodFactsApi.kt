package com.romain.pedepoy.inventory.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface OpenFoodFactsApi {
    @GET("{productId}.json")
    suspend fun getProductInfo(@Path("productId") ean: String): Response<ProductResponse>
}