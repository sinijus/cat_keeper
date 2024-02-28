package com.jaanussinivali.catkeeper.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.CatWithWeights

@Dao
interface CatDao {
    @Insert
    fun createCat(cat: Cat)
    @Transaction
    @Query("SELECT * FROM cat")
    fun getAllCats(): List<CatWithWeights>
    @Transaction
    @Query("SELECT * FROM cat WHERE id=:id")
    fun getCat(id: Int): CatWithWeights

    @Update
    fun updateCat(cat: Cat)

}