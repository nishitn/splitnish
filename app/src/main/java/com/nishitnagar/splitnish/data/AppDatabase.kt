package com.nishitnagar.splitnish.data

import android.content.Context
import androidx.room.*
import com.nishitnagar.splitnish.data.dao.*
import com.nishitnagar.splitnish.util.RoomTypeConvertors
import com.nishitnagar.splitnish.data.entity.*


@TypeConverters(RoomTypeConvertors::class)
@Database(
  entities = [
    AccountEntity::class,
    CategoryEntity::class,
    SubCategoryEntity::class,
    ChapterEntity::class,
    UserSelectionEntity::class,
    GroupSelectionEntity::class,
    TransactionEntity::class,
    GroupUserCrossRef::class,
    GroupEntity::class,
    UserEntity::class
  ],
  version = 1
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun accountDao(): AccountDao

  abstract fun categoryDao(): CategoryDao

  abstract fun chapterDao(): ChapterDao

  abstract fun transactionDao(): TransactionDao

  abstract fun userDao(): UserDao

  abstract fun groupDao(): GroupDao

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