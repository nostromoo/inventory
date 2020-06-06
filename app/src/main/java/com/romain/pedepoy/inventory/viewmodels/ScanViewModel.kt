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
import com.romain.pedepoy.inventory.service.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        barCode?.displayValue?.let { ean ->

            when {
                inputDate.value == null -> {
                    statusMessage.value = Event("You have to select a date")
                }
                inputDate.value?.before(Date()) == true -> {
                    statusMessage.value = Event("Date has to be in the future")
                }
                else -> {
                    inputDate.value?.let { date ->
                        withContext(Dispatchers.IO) {
                            when (products.value?.firstOrNull { it.id == ean }) {
                                null -> {
                                    retrieveProductInfo(ean, "Product Inserted Successfully", date)

                                }
                                else -> {
                                    retrieveProductInfo(ean, "Expiry date updated Successfully", date)
                                }
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

    private suspend fun retrieveProductInfo(ean: String, message: String, expiryDate: Date) {
        val result =  productRepository.fetchProductInfo(ean)
        when (result.status) {
            Result.Status.SUCCESS -> {
                result.data?.let { productResponse ->
                    productRepository.insert(Product(ean,expiryDate, productResponse.product?.product_name_fr, productResponse.product?.image_url))
                    CoroutineScope(Dispatchers.Main).launch{
                        statusMessage.value = Event(message)
                    }
                }
            }
            Result.Status.ERROR -> {
                productRepository.insert(Product(ean, expiryDate))
                CoroutineScope(Dispatchers.Main).launch{
                    statusMessage.value = Event(message)
                }
            }
        }
    }

    fun insertFromBarcode(barcode: FirebaseVisionBarcode) = viewModelScope.launch {
        barCode = barcode
    }
}
