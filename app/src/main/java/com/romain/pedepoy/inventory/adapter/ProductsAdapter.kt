package com.romain.pedepoy.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.databinding.ProductItemBinding

class ProductsAdapter :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private val productsList = ArrayList<Product>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    fun setList(products: List<Product>) {
        productsList.clear()
        productsList.addAll(products)
    }

    class ViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.product = product
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}
