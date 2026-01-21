package com.example.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE


@Dao
interface TransactionDao {

    @Insert(entity = TransactionEntity::class, onConflict = REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity)
}