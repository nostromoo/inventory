package com.romain.pedepoy.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.util.*


@Entity(tableName = "products")
@TypeConverters(DateConverter::class)
data class Product (
    @PrimaryKey
    var id: String,
    var expirationDate: Date,
    val name: String?= null,
    val picture: String?= null
){
    fun displayName(): String = if(name.isNullOrEmpty())"Name unavailable" else name

    fun displayDate(): String = "Expiration date : ${SimpleDateFormat("dd MMMM yyyy").format(expirationDate)}"

    fun displayBarcode(): String =  "Barcode : $id"
}