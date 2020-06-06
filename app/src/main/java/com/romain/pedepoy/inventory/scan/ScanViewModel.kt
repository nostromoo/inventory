package com.romain.pedepoy.inventory.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.romain.pedepoy.inventory.Event
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

    private val barCode = MutableLiveData<FirebaseVisionBarcode>()

    val getBarCode: LiveData<FirebaseVisionBarcode>
        get() = barCode

    private val _statusMessage = MutableLiveData<Event<String>>()

    val statusMessage: LiveData<Event<String>>
        get() = _statusMessage

    private val _editTaskEvent = MutableLiveData<Event<Unit>>()

    val editTaskEvent: LiveData<Event<Unit>>
            get() = _editTaskEvent

    fun validateExpirationDate(date: Date)  = viewModelScope.launch {
        barCode.value?.displayValue?.let { ean ->

            when {
                date.before(Date()) -> {
                    _statusMessage.value = Event("Date has to be in the future")
                }
                else -> {
                    withContext(Dispatchers.IO) {
                        when (products.value?.firstOrNull { it.id == ean }) {
                            null -> {
                                retrieveProductInfo(ean, "Product Inserted Successfully", date)

                            }
                            else -> {
                                retrieveProductInfo(ean, "Expiration date updated Successfully", date)
                            }
                        }
                    }

                }
            }
            editTask()
        }
    }

    fun editTask() {
        _editTaskEvent.value = Event(Unit)
    }

    private suspend fun retrieveProductInfo(ean: String, message: String, expirationDate: Date) {
        val result =  productRepository.fetchProductInfo(ean)
        when (result.status) {
            Result.Status.SUCCESS -> {
                result.data?.let { productResponse ->
                    productRepository.insert(Product(ean, expirationDate, productResponse.product?.product_name_fr, productResponse.product?.image_url))
                    CoroutineScope(Dispatchers.Main).launch{
                        _statusMessage.value = Event(message)
                    }
                }
            }
            Result.Status.ERROR -> {
                productRepository.insert(Product(ean, expirationDate))
                CoroutineScope(Dispatchers.Main).launch{
                    _statusMessage.value = Event(message)
                }
            }
        }
    }

    fun setBarcodeValue(barcode: FirebaseVisionBarcode) {
        barCode.value = barcode
    }
}
