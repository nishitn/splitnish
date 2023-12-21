package com.nishitnagar.splitnish.di

import android.app.Application
import com.nishitnagar.splitnish.data.AppDatabase
import com.nishitnagar.splitnish.data.dao.*
import com.nishitnagar.splitnish.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
  @Singleton
  @Provides
  fun provideAccountRepository(accountDao: AccountDao): AccountRepository {
    return AccountRepository(accountDao)
  }

  @Singleton
  @Provides
  fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
    return CategoryRepository(categoryDao)
  }

  @Singleton
  @Provides
  fun provideChapterRepository(chapterDao: ChapterDao): ChapterRepository {
    return ChapterRepository(chapterDao)
  }

  @Singleton
  @Provides
  fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
    return TransactionRepository(transactionDao)
  }

  @Singleton
  @Provides
  fun provideUserRepository(userDao: UserDao): UserRepository {
    return UserRepository(userDao)
  }

  @Singleton
  @Provides
  fun provideGroupRepository(groupDao: GroupDao): GroupRepository {
    return GroupRepository(groupDao)
  }

  @Singleton
  @Provides
  fun provideAccountDao(appDatabase: AppDatabase): AccountDao {
    return appDatabase.accountDao()
  }

  @Singleton
  @Provides
  fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
    return appDatabase.categoryDao()
  }

  @Singleton
  @Provides
  fun provideChapterDao(appDatabase: AppDatabase): ChapterDao {
    return appDatabase.chapterDao()
  }

  @Singleton
  @Provides
  fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
    return appDatabase.transactionDao()
  }

  @Singleton
  @Provides
  fun provideUserDao(appDatabase: AppDatabase): UserDao {
    return appDatabase.userDao()
  }

  @Singleton
  @Provides
  fun provideGroupDao(appDatabase: AppDatabase): GroupDao {
    return appDatabase.groupDao()
  }

  @Singleton
  @Provides
  fun provideAppDatabase(app: Application): AppDatabase {
    return AppDatabase.getInstance(app)
  }
}