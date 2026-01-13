package com.example.expensetracker.domain.service

import com.example.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetExpenseBetweenUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
){

    operator fun invoke(start : Long, end : Long) = expenseRepository.getExpenseBetween(start, end)
}