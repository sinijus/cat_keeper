package com.jaanussinivali.catkeeper.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jaanussinivali.catkeeper.data.entity.Cat

@Dao
interface CatDao {
    @Insert
    fun createCat(cat: Cat)

    @Query("SELECT * FROM cat")
    fun getAllCats(): List<Cat>

    @Query("SELECT id FROM cat")
    fun getCat(): Cat

    @Update
    fun updateCat(cat: Cat)

}