package com.romain.pedepoy.inventory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.romain.pedepoy.inventory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView<ActivityMainBinding>(this, R.layout.activity_main)
  }
}