package com.romain.pedepoy.inventory.viewmodels

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.data.ProductRepository
import kotlinx.coroutines.launch
import java.util.*

class ScanViewModel constructor(
    private val productRepository: ProductRepository
) : ViewModel(), Observable {

      var barCode: FirebaseVisionBarcode? = null
//    fun setDatePickerVisibility(visibility: Int) {
//        datePickerVisibility.value = visibility
//    }

    @Bindable
    val inputDate = MutableLiveData<Date>()

    fun validateExpiryDate()  = viewModelScope.launch {
        barCode?.displayValue?.let {
            productRepository.insert(Product(it, inputDate.value!!, "name"))
        }
    }

    fun insertFromBarcode(barcode: FirebaseVisionBarcode) = viewModelScope.launch {
        barCode = barcode
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}
