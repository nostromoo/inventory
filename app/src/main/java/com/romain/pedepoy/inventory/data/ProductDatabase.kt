package com.romain.pedepoy.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class],version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object{
        @Volatile
        private var INSTANCE : ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ProductDatabase {
            return Room.databaseBuilder(context, ProductDatabase::class.java, "product_database")
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}

