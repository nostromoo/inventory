package com.romain.pedepoy.inventory.data

class ProductRepository(private val dao : ProductDao) {

    val products = dao.getAllProducts()

    suspend fun insert(product: Product){
        return dao.insert(product)
    }

    companion object {

        const val INITIAL_LOAD = 60
        const val PAGE_SIZE = 20
        const val PRE_FETCH_DISTANCE = 10

    }
}