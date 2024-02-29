package com.jaanussinivali.catkeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.General
import com.jaanussinivali.catkeeper.data.entity.Insurance
import com.jaanussinivali.catkeeper.data.entity.Medical
import com.jaanussinivali.catkeeper.data.entity.Weight

@Database(
    entities = [Cat::class, Weight::class, General::class, Medical::class, Insurance::class],
    version = 1
)
abstract class CatKeeperDatabase : RoomDatabase() {
    abstract fun getCatDao(): CatDao

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: CatKeeperDatabase? = null
        fun getDatabase(context: Context): CatKeeperDatabase {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                DATABASE_INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context, CatKeeperDatabase::class.java, "cat-keeper-database"
        )
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        val catDao = DATABASE_INSTANCE?.getCatDao()
                        catDao?.insert(Cat(name = "Cat One"))
                        catDao?.insert(
                            General(
                                catId = 1,
                                officialName = "Tips",
                                birthDate = "111",
                                birthPlace = "111",
                                age = 2
                            )
                        )
                        catDao?.insert(
                            Medical(
                                catId = 1,
                                lastDoctorVisit = "111",
                                lastWormMedicine = "111",
                                lastVaccination = "111"
                            )
                        )
                        catDao?.insert(
                            Insurance(
                                catId = 1,
                                company = "If",
                                phone = "111",
                                validUntil = "22.11.2024",
                                sum = 1000
                            )
                        )
                        catDao?.insert(Cat(name = "Cat Two"))
                        catDao?.insert(
                            General(
                                catId = 2,
                                officialName = "",
                                birthDate = "",
                                birthPlace = "",
                                age = 0
                            )
                        )
                        catDao?.insert(
                            Medical(
                                catId = 2,
                                lastDoctorVisit = "",
                                lastWormMedicine = "",
                                lastVaccination = ""
                            )
                        )
                        catDao?.insert(
                            Insurance(
                                catId = 2,
                                company = "",
                                phone = "",
                                validUntil = "",
                                sum = 0
                            )
                        )
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}