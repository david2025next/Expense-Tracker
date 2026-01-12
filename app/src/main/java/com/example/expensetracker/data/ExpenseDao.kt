package com.example.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(expenseEntity: ExpenseEntity)

    @Query("""
        SELECT * FROM Expenses
        ORDER BY createdAt DESC
        LIMIT :limit
    """)
    fun getRecentExpenses(limit : Int) : Flow<List<ExpenseEntity>>

    @Query("""
    SELECT
        SUM(CASE WHEN createdAt BETWEEN :todayStart AND :todayEnd THEN amount ELSE 0 END) AS todayTotal,
        SUM(CASE WHEN createdAt BETWEEN :weekStart AND :weekEnd THEN amount ELSE 0 END) AS weekTotal,
        SUM(CASE WHEN createdAt BETWEEN :monthStart AND :monthEnd THEN amount ELSE 0 END) AS monthTotal
    FROM Expenses
""")
    fun getExpensePeriodTotals(
        todayStart: Long,
        todayEnd: Long,
        weekStart: Long,
        weekEnd: Long,
        monthStart: Long,
        monthEnd: Long
    ): Flow<ExpensePeriodTotalsEntity>

    @Query("SELECT * FROM expenses WHERE createdAt BETWEEN :start AND :end")
    fun filterExpense(start : Long, end : Long) : Flow<List<ExpenseEntity>>
}