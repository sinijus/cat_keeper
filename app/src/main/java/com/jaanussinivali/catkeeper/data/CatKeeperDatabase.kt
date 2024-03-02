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
    version = 2
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
                                officialName = "",
                                birthDate = "",
                                birthPlace = "",
                                age = ""
                            ),
                        )
                        catDao?.insert(
                            Medical(
                                catId = 1,
                                lastDoctorVisit = "",
                                lastWormMedicine = "",
                                lastVaccination = ""
                            )
                        )
                        catDao?.insert(
                            Insurance(
                                catId = 1,
                                company = "",
                                phone = "",
                                validUntil = "",
                                sum = ""
                            )
                        )
                        catDao?.insert(Cat(name = "Cat Two"))
                        catDao?.insert(
                            General(
                                catId = 2,
                                officialName = "",
                                birthDate = "",
                                birthPlace = "",
                                age = ""
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
                                sum = ""
                            )
                        )
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}