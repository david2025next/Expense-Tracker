package com.example.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TransactionDao {

    @Insert(entity = TransactionEntity::class, onConflict = REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity)

    @Query(
        """
        SELECT
            SUM(CASE WHEN transactionType = 'INCOME' THEN amount ELSE 0 END ) as totalsIncome,
            SUM(CASE WHEN transactionType = 'EXPENSE' THEN amount ELSE 0 END ) as totalsExpense
        FROM `TRANSACTION`
    """
    )
    fun getBalanceSummary(): Flow<BalanceSummaryEntity>

    @Query(
        """
        SELECT
         SUM(CASE WHEN (transactionType = 'INCOME' and (date BETWEEN :start and :end)) THEN amount ELSE 0 END) as totalsIncome,
         SUM(CASE WHEN (transactionType = 'EXPENSE' and (date BETWEEN :start and :end)) THEN amount ELSE 0 END) as totalsExpense
        FROM `TRANSACTION`
    """
    )
    fun getTotalsSpentPeriodTotals(
        start: Long,
        end: Long
    ): Flow<TransactionPeriodTotalsSpent>


    @Query(
        """
        SELECT * FROM `TRANSACTION` WHERE date BETWEEN :start and :end ORDER BY date DESC
    """
    )
    fun getTransactionByPeriod(
        start: Long,
        end: Long
    ): Flow<List<TransactionEntity>>
}