package com.romain.pedepoy.inventory.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.romain.pedepoy.inventory.Event
import com.romain.pedepoy.inventory.R
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.data.ProductRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ScanViewModel @Inject constructor(
    val productRepository: ProductRepository
) : ViewModel() {

    val products = productRepository.products

    private var barCode: FirebaseVisionBarcode? = null

    val inputDate = MutableLiveData<Date>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage


    fun validateExpiryDate(v : View)  = viewModelScope.launch {
        barCode?.displayValue?.let { displayValue ->

            when {
                inputDate.value == null -> {
                    statusMessage.value = Event("You have to select a date")
                }
                inputDate.value?.before(Date()) == true -> {
                    statusMessage.value = Event("Date has to be in the future")
                }
                else -> {
                    inputDate.value?.let { date ->
                        when (products.value?.firstOrNull { it.id == displayValue }) {
                            null -> {
                                productRepository.insert(Product(displayValue, date))
                                statusMessage.value = Event("Product Inserted Successfully")
                            }
                            else -> {
                                productRepository.insert(Product(displayValue, date))
                                statusMessage.value = Event("Expiry date updated Successfully")
                            }
                        }

                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.productListFragment, true).build()
                        val navController = Navigation.findNavController(v)
                        navController.navigate(R.id.action_scanFragment_to_productListFragment,null, navOptions)
                    }
                }
            }
        }
    }

    fun insertFromBarcode(barcode: FirebaseVisionBarcode) = viewModelScope.launch {
        barCode = barcode
    }
}
