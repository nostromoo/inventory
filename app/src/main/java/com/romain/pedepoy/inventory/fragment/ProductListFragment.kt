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
import com.romain.pedepoy.inventory.dagger.Injectable
import com.romain.pedepoy.inventory.dagger.injectViewModel
import com.romain.pedepoy.inventory.databinding.FragmentProductListBinding
import com.romain.pedepoy.inventory.viewmodels.ProductListViewModel
import javax.inject.Inject

class ProductListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productlistViewModel: ProductListViewModel
    private lateinit var adapter: ProductsAdapter
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductListBinding.inflate(inflater, container, false)

        productlistViewModel = injectViewModel(viewModelFactory)

        binding.myViewModel = productlistViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

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