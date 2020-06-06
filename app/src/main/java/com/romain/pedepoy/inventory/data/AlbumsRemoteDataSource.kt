package com.romain.pedepoy.inventory.data

import com.romain.pedepoy.inventory.service.OpenFoodFactsApi
import com.romain.pedepoy.inventory.service.BaseDataSource
import javax.inject.Inject


class ProductsRemoteDataSource @Inject constructor(private val service: OpenFoodFactsApi) : BaseDataSource() {

    suspend fun fetchProductInfo(ean: String) = getResult { service.getProductInfo(ean) }
}
