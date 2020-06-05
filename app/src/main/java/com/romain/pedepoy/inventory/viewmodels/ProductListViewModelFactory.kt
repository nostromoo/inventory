package com.romain.pedepoy.inventory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romain.pedepoy.inventory.data.ProductRepository
import java.lang.IllegalArgumentException

class ProductListViewModelFactory(private val repository: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProductListViewModel::class.java)){
            return ProductListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}