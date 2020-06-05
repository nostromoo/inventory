package com.romain.pedepoy.inventory.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.romain.pedepoy.inventory.R
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.data.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products = productRepository.products

    fun scanProduct(v: View) {
        val navController = v.findNavController()
        navController.navigate(R.id.action_productListFragment_to_scanFragment)
    }

    fun insert(product:Product) = viewModelScope.launch {
        productRepository.insert(product)
    }
}
