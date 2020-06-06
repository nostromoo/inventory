package com.romain.pedepoy.inventory.dagger

import com.romain.pedepoy.inventory.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMaintActivity(): MainActivity
}
