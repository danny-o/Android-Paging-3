package com.digitalskies.androidpaging3.ui.mainactivity

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.digitalskies.androidpaging3.model.ArticleSearch.ArticleDetails
import com.digitalskies.androidpaging3.model.ArticleSearchAndArticles
import com.digitalskies.androidpaging3.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class MainActivityViewModel(application: Application,var newsRepository: NewsRepository):AndroidViewModel(application) {
    var articleSearchResult: Flow<PagingData<ArticleDetails>>?=null

    var lastSearch:String?=null





  suspend  fun getArticles(search:String):Flow<PagingData<ArticleDetails>>{


            if(lastSearch==search){
                articleSearchResult?.let {articleSearchResult_->
                    return articleSearchResult_
                }
            }


      val newSearchResult:Flow<PagingData<ArticleDetails>> =newsRepository
              .initSearch(search)
              .cachedIn(viewModelScope)

          articleSearchResult=newSearchResult


          lastSearch=search

          return newSearchResult



    }

    suspend fun getSearchResultsCount(query:String): Int? {
       return newsRepository.getSearchResultsCount(query)
    }



}