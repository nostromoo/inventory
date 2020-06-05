package com.romain.pedepoy.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*


@Entity(tableName = "products")
@TypeConverters(DateConverter::class)
data class Product (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val expiryDate: Date,
    val name: String
)