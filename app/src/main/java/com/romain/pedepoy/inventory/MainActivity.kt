package com.romain.pedepoy.inventory

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.romain.pedepoy.inventory.adapter.ProductsAdapter
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.data.ProductDatabase
import com.romain.pedepoy.inventory.data.ProductRepository
import com.romain.pedepoy.inventory.databinding.ActivityMainBinding
import com.romain.pedepoy.inventory.viewmodels.ProductListViewModel
import com.romain.pedepoy.inventory.viewmodels.ProductListViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity()
{
  private lateinit var binding: ActivityMainBinding
  private lateinit var productlistViewModel: ProductListViewModel
  private lateinit var adapter: ProductsAdapter

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
    val dao = ProductDatabase.getInstance(application).productDao()
    val repository = ProductRepository(dao)
    val factory = ProductListViewModelFactory(repository)
    productlistViewModel = ViewModelProvider(this,factory).get(ProductListViewModel::class.java)
    binding.myViewModel = productlistViewModel
    binding.lifecycleOwner = this
    initRecyclerView()

    productlistViewModel.insert(Product(0, Date(),"Evian"))

  }

  private fun initRecyclerView(){
    binding.productList.layoutManager = LinearLayoutManager(this)
    adapter = ProductsAdapter()
    binding.productList.adapter = adapter
    displayProductsList()
  }

  private fun displayProductsList(){
    productlistViewModel.products.observe(this, Observer {
      Log.i("MYTAG",it.toString())
      adapter.setList(it)
      adapter.notifyDataSetChanged()
    })
  }
}
