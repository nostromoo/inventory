package com.romain.pedepoy.inventory.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.romain.pedepoy.inventory.R
import com.romain.pedepoy.inventory.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScanBinding.inflate(inflater, container, false)


        return binding.root
    }
}