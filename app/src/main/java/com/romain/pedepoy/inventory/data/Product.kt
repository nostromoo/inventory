package com.romain.pedepoy.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*


@Entity(tableName = "products")
@TypeConverters(DateConverter::class)
data class Product (
    @PrimaryKey
    val id: String,
    val expiryDate: Date,
    val name: String
)