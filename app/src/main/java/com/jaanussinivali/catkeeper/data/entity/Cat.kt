package com.jaanussinivali.catkeeper.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "general")
data class General(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val catId: Int?,
    @ColumnInfo(name = "official_name") val officialName: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?,
    val age: Int?,
    @ColumnInfo(name = "birth_place") val birthPlace: String?,
)

@Entity(tableName = "weight")
data class Weight(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String?,
    val value: Float?
)

@Entity(tableName = "medical")
data class Medical(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val catId: Int?,
    @ColumnInfo(name = "last_doctor_visit") val lastDoctorVisit: String?,
    @ColumnInfo(name = "last_worm_medicine") val lastWormMedicine: String?,
    @ColumnInfo(name = "last_vaccination") val lastVaccination: String?,
)

@Entity(tableName = "insurance")
data class Insurance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val catId: Int?,
    val company: String?,
    val phone: String?,
    @ColumnInfo(name = "valid_until") val validUntil: String?,
    val sum: Int?
)

@Entity(tableName = "cat")
data class Cat(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
)

//data class CatWithGeneralMedicalInsurance(
//    @Embedded val cat: Cat,
//    @Relation(parentColumn = "id", entityColumn = "catId")
//    val general: General,
//    val medical: Medical,
//    val insurance: Insurance
//)



