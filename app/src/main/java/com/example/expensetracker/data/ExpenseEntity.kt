package com.example.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "Expenses"
)
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val title : String,
    val date : Long,
    val amount : Long,
    val category : String
)
