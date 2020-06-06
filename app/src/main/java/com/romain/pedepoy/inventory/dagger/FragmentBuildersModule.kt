package com.romain.pedepoy.inventory.dagger


import com.romain.pedepoy.inventory.productList.ProductListFragment
import com.romain.pedepoy.inventory.scan.ScanFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun contributeScanFragment(): ScanFragment

}
