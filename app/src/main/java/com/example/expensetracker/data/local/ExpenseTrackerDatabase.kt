package com.example.expensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransactionConverter::class)
abstract class ExpenseTrackerDatabase : RoomDatabase() {

    abstract val transactionDao : TransactionDao
}