package com.digitalskies.androidpaging3.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.digitalskies.androidpaging3.model.ArticlePageKeys
import com.digitalskies.androidpaging3.model.ArticleSearch
import com.digitalskies.androidpaging3.model.ArticleSearch.ArticleDetails

@Database(entities = [ArticleSearch::class, ArticleDetails::class,ArticlePageKeys::class],version = 1,exportSchema = false)
abstract class ArticlesDatabase:RoomDatabase() {

    abstract fun getArticlesDao():ArticlesDao
    abstract fun getKeysDao():KeysDao


    companion object{
        val DATABASE_NAME="articles_database"

    }


}