package com.romain.pedepoy.inventory.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romain.pedepoy.inventory.viewmodels.ProductListViewModel
import com.romain.pedepoy.inventory.viewmodels.ScanViewModel
import com.romain.pedepoy.inventory.viewmodels.ViewModelsFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductListViewModel::class)
    abstract fun bindProductListViewModel(viewModel: ProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanViewModel::class)
    abstract fun bindScanViewModel(viewModel: ScanViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelsFactory): ViewModelProvider.Factory

}
