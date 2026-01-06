package com.example.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Insert(onConflict = REPLACE)
    fun insert(expenseEntity: ExpenseEntity)

    @Query("""
        SELECT * FROM Expenses
        ORDER BY date DESC
        LIMIT :limit
    """)
    fun getRecentExpenses(limit : Int) : Flow<List<ExpenseEntity>>

    @Query("""
    SELECT
        SUM(CASE WHEN date BETWEEN :todayStart AND :todayEnd THEN amount ELSE 0 END) AS todayTotal,
        SUM(CASE WHEN date BETWEEN :weekStart AND :weekEnd THEN amount ELSE 0 END) AS weekTotal,
        SUM(CASE WHEN date BETWEEN :monthStart AND :monthEnd THEN amount ELSE 0 END) AS monthTotal
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
}