package com.romain.pedepoy.inventory

import android.app.Activity
import android.app.Application
import com.romain.pedepoy.inventory.dagger.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class InventoryApplication : Application(), HasActivityInjector  {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>  = dispatchingAndroidInjector
}