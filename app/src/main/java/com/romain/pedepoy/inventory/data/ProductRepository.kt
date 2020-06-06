package com.romain.pedepoy.inventory.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val dao : ProductDao) {

    val products = dao.getAllProducts()

    suspend fun insert(product: Product){
        return dao.insert(product)
    }
}