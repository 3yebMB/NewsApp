package dev.m13d.newsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.m13d.newsapp.model.entity.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDaoFun(): NewsDao
}
