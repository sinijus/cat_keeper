package com.jaanussinivali.catkeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.Weight

@Database(entities = [Cat::class, Weight::class], version = 2)
abstract class CatKeeperDatabase: RoomDatabase() {
    abstract fun getCatDao(): CatDao

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: CatKeeperDatabase? = null
        fun getDatabase(context: Context): CatKeeperDatabase {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context, CatKeeperDatabase::class.java, "cat-keeper-database"
                ).fallbackToDestructiveMigration().build()
                DATABASE_INSTANCE = instance
                instance
            }
        }
    }
}