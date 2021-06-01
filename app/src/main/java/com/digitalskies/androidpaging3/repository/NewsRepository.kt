package com.digitalskies.androidpaging3.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.digitalskies.androidpaging3.data.ArticleRemoteMediator
import com.digitalskies.androidpaging3.data.ArticlesDatabase
import com.digitalskies.androidpaging3.model.ArticleSearch
import com.digitalskies.androidpaging3.model.ArticleSearch.ArticleDetails
import com.digitalskies.androidpaging3.model.ArticleSearchAndArticles
import com.digitalskies.androidpaging3.network.NewsClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRepository @Inject constructor(var articlesDatabase: ArticlesDatabase) {
    var totalResults:MutableLiveData<Int> =MutableLiveData()


    suspend fun initSearch(search:String):Flow<PagingData<ArticleDetails>>{


        val dbQuery=search.replace(' ','%')
       // totalResults=articlesDatabase.getArticlesDao().getSearchResultsCount(search)


        val pagingSourceFactory={articlesDatabase.getArticlesDao().getArticles(dbQuery)}


        return Pager(

            config = PagingConfig(pageSize = PAGE_SIZE,enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ArticleRemoteMediator(search,articlesDatabase)
        ).flow.also { totalResults.value=articlesDatabase.getArticlesDao().getSearchResultsCount(search) }


    }

   suspend fun getSearchResultsCount(query:String): Int? {

        return articlesDatabase.getArticlesDao().getSearchResultsCount(query)
    }
    




    companion object{
        const val PAGE_SIZE=30
    }
}