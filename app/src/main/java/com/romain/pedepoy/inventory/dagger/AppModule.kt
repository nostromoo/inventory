package com.romain.pedepoy.inventory.dagger

import android.app.Application
import com.romain.pedepoy.inventory.service.OpenFoodFactsApi
import com.romain.pedepoy.inventory.data.ProductDatabase
import com.romain.pedepoy.inventory.data.ProductsRemoteDataSource
import com.romain.pedepoy.inventory.utilities.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideAlbumApi() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(OpenFoodFactsApi::class.java)

    @Singleton
    @Provides
    fun provideAlbumsRemoteDataSource(albumApi: OpenFoodFactsApi)
            =  ProductsRemoteDataSource(albumApi)

    @Singleton
    @Provides
    fun provideDb(app: Application) = ProductDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideProductDao(db: ProductDatabase) = db.productDao()

}
