package com.romain.pedepoy.inventory.service


data class ProductResponse (
    val product: Product?= null
){
    data class Product (
        val product_name_fr: String? = null,
        val image_url: String? = null)
}