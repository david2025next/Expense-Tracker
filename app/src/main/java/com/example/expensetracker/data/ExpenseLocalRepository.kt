package com.example.expensetracker.data

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.service.ExpensePeriodTotals
import com.example.expensetracker.utils.monthRange
import com.example.expensetracker.utils.todayRange
import com.example.expensetracker.utils.weekRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseLocalRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {


    override fun getExpensePeriodTotals(): Flow<ExpensePeriodTotals> =
        expenseDao.getExpensePeriodTotals(
            todayStart = todayRange().start,
            todayEnd = todayRange().end,
            weekStart = weekRange().start,
            weekEnd = weekRange().end,
            monthStart = monthRange().start,
            monthEnd = monthRange().end
        ).map { it.toExpensePeriodTotalDomain() }

    override fun getRecentExpenses(limit: Int): Flow<List<Expense>> =
        expenseDao.getRecentExpenses(limit)
            .map { expenseEntities -> expenseEntities.map { it.toExpenseDomain() }.sortedByDescending { it.date } }

    override suspend fun addExpense(expense: Expense) = expenseDao.insert(expense.toEntity())
    override fun filter(
        start: Long,
        end: Long
    ): Flow<List<Expense>> = expenseDao.filterExpense(start, end)
        .map { expenseEntities -> expenseEntities.map { it.toExpenseDomain() }.sortedByDescending { it.date } }

}


fun Expense.toEntity() = ExpenseEntity(
    title = title,
    amount = amount,
    createdAt = date,
    category = category
)

private fun ExpenseEntity.toExpenseDomain() = Expense(
    title = title,
    amount = amount,
    date = createdAt,
    category = category,
)

private fun ExpensePeriodTotalsEntity.toExpensePeriodTotalDomain() = ExpensePeriodTotals(
    today = todayTotal,
    thisWeek = weekTotal,
    thisMonth = monthTotal
)
