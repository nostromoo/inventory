package com.romain.pedepoy.inventory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romain.pedepoy.inventory.productList.ProductListViewModel
import com.romain.pedepoy.inventory.data.ProductRepository
import com.romain.pedepoy.inventory.scan.ScanViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ViewModelsFactory @Inject constructor(private val repository: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProductListViewModel::class.java)){
            return ProductListViewModel(
                repository
            ) as T
        } else if(modelClass.isAssignableFrom(ScanViewModel::class.java)){
            return ScanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}