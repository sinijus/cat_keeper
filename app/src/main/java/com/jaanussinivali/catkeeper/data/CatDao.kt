package com.jaanussinivali.catkeeper.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jaanussinivali.catkeeper.data.entity.Cat
import com.jaanussinivali.catkeeper.data.entity.General
import com.jaanussinivali.catkeeper.data.entity.Insurance
import com.jaanussinivali.catkeeper.data.entity.Medical

@Dao
interface CatDao {

    @Insert
    fun insert(cat: Cat)

    @Insert
    fun insert(general: General)

    @Insert
    fun insert(medical: Medical)

    @Insert
    fun insert(insurance: Insurance)

    @Query("SELECT*FROM general WHERE id=:id")
    fun getGeneral(id: Int): General

    @Query("SELECT*FROM medical WHERE id=:id")
    fun getMedical(id: Int): Medical

    @Query("SELECT*FROM insurance WHERE id=:id")
    fun getInsurance(id: Int): Insurance

    @Query("SELECT*FROM cat WHERE id=:id")
    fun getCat(id: Int): Cat

    @Update
    fun update(cat: Cat)

    @Update
    fun update(general: General)

    @Update
    fun update(medical: Medical)

    @Update
    fun update(insurance: Insurance)


}