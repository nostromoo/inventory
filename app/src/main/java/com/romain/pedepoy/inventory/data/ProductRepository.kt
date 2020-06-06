package com.romain.pedepoy.inventory.data

import com.romain.pedepoy.inventory.service.ProductResponse
import javax.inject.Inject
import javax.inject.Singleton
import  com.romain.pedepoy.inventory.service.Result
@Singleton
class ProductRepository @Inject constructor(private val dao : ProductDao,
                                            private val remoteSource: ProductsRemoteDataSource) {

    val products = dao.getAllProducts()

    suspend fun insert(product: Product){
        return dao.insert(product)
    }

    suspend fun fetchProductInfo(ean: String): Result<ProductResponse>{
        return remoteSource.fetchProductInfo(ean)
    }
}