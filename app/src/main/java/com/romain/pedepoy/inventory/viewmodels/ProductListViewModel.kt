package com.romain.pedepoy.inventory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.data.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products = productRepository.products

    fun scanProduct() {
        //TODO
    }

    fun insert(product:Product) = viewModelScope.launch {
        productRepository.insert(product)
    }
}
