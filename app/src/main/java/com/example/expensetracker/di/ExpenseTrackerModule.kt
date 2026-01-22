package com.example.expensetracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.expensetracker.data.TransactionRepositoryImpl
import com.example.expensetracker.data.datastore.UserPreferences
import com.example.expensetracker.data.local.ExpenseTrackerDatabase
import com.example.expensetracker.data.local.TransactionDao
import com.example.expensetracker.domain.repository.TransactionRepository
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
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesExpenseTrackerDatabase(@ApplicationContext context: Context): ExpenseTrackerDatabase =
        Room.databaseBuilder(
            context, ExpenseTrackerDatabase::class.java, "expenseTrackerDatabase"
        )
            .fallbackToDestructiveMigration(true)
            .build()

    @Singleton
    @Provides
    fun providesTransactionDao(expenseTrackerDatabase: ExpenseTrackerDatabase): TransactionDao =
        expenseTrackerDatabase.transactionDao


    @Singleton
    @Provides
    fun providesDatastore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(UserPreferences.DATA) },
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
        )

}