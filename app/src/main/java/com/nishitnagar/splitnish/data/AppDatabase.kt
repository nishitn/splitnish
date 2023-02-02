package com.nishitnagar.splitnish.data

import android.content.Context
import androidx.room.*
import com.nishitnagar.splitnish.Convertors
import com.nishitnagar.splitnish.data.dao.AccountDao
import com.nishitnagar.splitnish.data.dao.CategoryDao
import com.nishitnagar.splitnish.data.dao.ChapterDao
import com.nishitnagar.splitnish.data.dao.TransactionDao
import com.nishitnagar.splitnish.data.entity.*


@TypeConverters(Convertors::class)
@Database(
    entities = [AccountEntity::class, CategoryEntity::class, ChapterEntity::class, UserSelectionEntity::class, GroupSelectionEntity::class, TransactionEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    abstract fun chapterDao(): ChapterDao

    abstract fun transactionDao(): TransactionDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "splitnish.db")
                    .fallbackToDestructiveMigration().build()
            }
            return INSTANCE as AppDatabase
        }
    }
}