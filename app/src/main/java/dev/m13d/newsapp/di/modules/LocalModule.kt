package dev.m13d.newsapp.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.m13d.newsapp.db.NewsDao
import dev.m13d.newsapp.db.NewsDatabase
import javax.inject.Singleton

@Module
class LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): NewsDatabase {
        return Room
            .databaseBuilder(application, NewsDatabase::class.java, "news_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideUserDao(appDataBase: NewsDatabase): NewsDao {
        return appDataBase.newsDaoFun()
    }
}
