package com.romain.pedepoy.inventory.dagger

import android.app.Application
import com.romain.pedepoy.inventory.data.ProductDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application) = ProductDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideProductDao(db: ProductDatabase) = db.productDao()

}
