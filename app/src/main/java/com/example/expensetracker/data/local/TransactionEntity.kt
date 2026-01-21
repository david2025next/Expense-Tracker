package com.example.expensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetracker.domain.model.TransactionType

@Entity(tableName = "Transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val description : String,
    val date : Long,
    val amount : Long,
    val category : String,
    val transactionType: TransactionType
)
