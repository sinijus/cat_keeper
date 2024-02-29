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

    fun insertOrUpdate(cat: Cat){
        if (getCat(cat.id) == null) insert(cat) else update(cat)
    }
    fun insertOrUpdate(general: General){
        if (getGeneral(general.id) == null) insert(general) else update(general)
    }
    fun insertOrUpdate(medical: Medical){
        if (getMedical(medical.id) == null) insert(medical) else update(medical)
    }
    fun insertOrUpdate(insurance: Insurance){
        if (getInsurance(insurance.id) == null) insert(insurance) else update(insurance)
    }
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