package com.romain.pedepoy.inventory.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.romain.pedepoy.inventory.adapter.ProductsAdapter
import com.romain.pedepoy.inventory.data.Product
import com.romain.pedepoy.inventory.data.ProductDatabase
import com.romain.pedepoy.inventory.data.ProductRepository
import com.romain.pedepoy.inventory.databinding.FragmentProductListBinding
import com.romain.pedepoy.inventory.viewmodels.ProductListViewModel
import com.romain.pedepoy.inventory.viewmodels.ProductListViewModelFactory
import java.util.*

class ProductListFragment : Fragment() {
    private lateinit var binding: FragmentProductListBinding
    private lateinit var productlistViewModel: ProductListViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        val dao = ProductDatabase.getInstance(requireContext()).productDao()
        val repository = ProductRepository(dao)
        val factory = ProductListViewModelFactory(repository)
        productlistViewModel = ViewModelProvider(this,factory).get(ProductListViewModel::class.java)
        binding.myViewModel = productlistViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        productlistViewModel.insert(Product(0, Date(),"Evian"))

        return binding.root
    }

    private fun initRecyclerView(){
        binding.productList.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductsAdapter()
        binding.productList.adapter = adapter
        displayProductsList()
    }

    private fun displayProductsList(){
        productlistViewModel.products.observe(viewLifecycleOwner){
            Log.i("MYTAG",it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }
}