package com.example.expensetracker.data.local

import androidx.room.TypeConverter
import com.example.expensetracker.domain.model.TransactionType

class TransactionConverter {

    @TypeConverter
    fun toTransactionType(displayName : String) = TransactionType.valueOf(displayName)

    @TypeConverter
    fun fromTransactionType(transactionType: TransactionType) = transactionType.name
}