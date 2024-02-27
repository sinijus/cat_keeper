package com.jaanussinivali.catkeeper.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class General(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "official_name") val officialName: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?,
    val age: Int?,
    @ColumnInfo(name = "birth_place") val birthPlace: String?,
)

data class Medical(
    @ColumnInfo(name = "last_doctor_visit") val lastDoctorVisit: String?,
    @ColumnInfo(name = "last_worm_medicine") val lastWormMedicine: String?,
    @ColumnInfo(name = "last_vaccination") val lastVaccination: String?,
    @Embedded(prefix = "weight_") val weight: Weight?
)

data class Insurance(
    val company: String?,
    val phone: String?,
    @ColumnInfo(name = "valid_until") val validUntil: String?,
    val sum: Int?
)

data class CatWithWeights(
    @Embedded val cat: Cat,
    @Relation(
        parentColumn = "id",
        entityColumn = "catId"
    )
    val weights: List<Weight>
)

@Entity(tableName = "cat")
data class Cat(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @Embedded(prefix = "general_") val general: General?,
    @Embedded(prefix = "medical_") val medical: Medical?,
    @Embedded(prefix = "ins_") val insurance: Insurance?
)

@Entity(tableName = "weight")
data class Weight(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val catId: Int,
    val date: String?,
    val value: Float?
)



