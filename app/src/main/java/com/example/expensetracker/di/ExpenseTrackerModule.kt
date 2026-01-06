package com.example.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.data.ExpenseLocalRepository
import com.example.expensetracker.data.ExpenseTrackerDatabase
import com.example.expensetracker.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindExpenseRepository(expenseLocalRepository: ExpenseLocalRepository): ExpenseRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesExpenseTrackerDatabase(@ApplicationContext context: Context): ExpenseTrackerDatabase =
        Room.databaseBuilder(
            context,
            ExpenseTrackerDatabase::class.java,
            "expensesTrackerDatabase"
        )
            .fallbackToDestructiveMigration(false)
            .build()

    @Singleton
    @Provides
    fun providesExpenseDao (expenseTrackerDatabase: ExpenseTrackerDatabase) = expenseTrackerDatabase.expenseDao
}