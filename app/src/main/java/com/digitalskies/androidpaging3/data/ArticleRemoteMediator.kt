package com.digitalskies.androidpaging3.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.digitalskies.androidpaging3.model.ArticlePageKeys
import com.digitalskies.androidpaging3.model.ArticleSearch
import com.digitalskies.androidpaging3.model.ArticleSearch.ArticleDetails
import com.digitalskies.androidpaging3.model.ArticleSearchAndArticles
import com.digitalskies.androidpaging3.network.NewsApi
import com.digitalskies.androidpaging3.network.NewsClient
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(var query:String,var articlesDatabase: ArticlesDatabase):RemoteMediator<Int, ArticleDetails>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleDetails>
    ): MediatorResult {
        val pageToLoad=when(loadType){


            LoadType.REFRESH-> {
                val pageToLoad=getRefreshKey(state)?.nextKey?.minus(1)?: STARTING_PAGE

                pageToLoad
            }
            LoadType.PREPEND->{
                val articlePageKeys=getPreviousKey(state)
                val pageToLoad= articlePageKeys?.prevKey ?: return MediatorResult.Success(articlePageKeys==null)
                pageToLoad


            }
            LoadType.APPEND->{
                val articlePageKeys=getNextKey(state)
                val pageToLoad= articlePageKeys?.nextKey ?: return MediatorResult.Success(articlePageKeys==null)
                pageToLoad
            }
        }
        try {
           val articleSearch:ArticleSearch= NewsClient.getNewsApi()
                   .getNewsArticles(query = query,pageSize = PAGE_SIZE,page = pageToLoad,apiKey = NewsClient.API_KEY)



            val endOfPaginationReached:Boolean= articleSearch.articles?.isEmpty() == true


            articleSearch.search=query

            val articlePageKeys=articleSearch.articles?.map { articleDetails ->

                    val nextPage=if(endOfPaginationReached) null else pageToLoad.plus(1)
                    val prevPage=if(pageToLoad== STARTING_PAGE) null else pageToLoad.minus(1)

                    articleDetails.search=query

                    articleDetails.prevKey=prevPage
                    articleDetails.nextKey=nextPage

                    ArticlePageKeys(prevKey = prevPage,nextKey = nextPage,articleTitle = articleDetails.title)
            }


            articlesDatabase.withTransaction {

                if(loadType==LoadType.REFRESH){

                    articlesDatabase.getArticlesDao().deleteSearch()
                    articlesDatabase.getArticlesDao().deleteArticles()
                    articlesDatabase.getKeysDao().deletePageKeys()

                }

                articleSearch.articles?.let {
                    articlesDatabase.getArticlesDao().insertArticles(it)
                }




                articlesDatabase.getArticlesDao().insertArticleSearch(articleSearch)
                articlePageKeys?.let {
                    articlesDatabase.getKeysDao().insertPageKeys(it) }


            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        }
        catch (httpException:HttpException){

            return MediatorResult.Error(httpException)
        }
        catch (ioException: IOException){

            return MediatorResult.Error(ioException)
        }


    }

    private suspend fun getPreviousKey(state: PagingState<Int, ArticleDetails>):ArticlePageKeys?{

      return state.pages.firstOrNull(){
          it.data.isNotEmpty()

      }?.data?.firstOrNull()?.let{articleDetails->

          articleDetails.title.let {

               articlesDatabase.getKeysDao().getArticleKeysByTitle(it)
           }

       }


    }
    private suspend fun getNextKey(state: PagingState<Int, ArticleDetails>):ArticlePageKeys?{
        return state.pages.lastOrNull{
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let{articleDetails->
                articleDetails.title.let {

                val result=articlesDatabase.getKeysDao().getArticleKeysByTitle(it)

                 result
            }

        }


    }

    private suspend fun getRefreshKey(state: PagingState<Int, ArticleDetails>):ArticlePageKeys?{

       return state.anchorPosition?.let {anchorPosition->

        state.closestItemToPosition(anchorPosition)?.title?.let { articleTitle->

            articlesDatabase.getKeysDao().getArticleKeysByTitle(articleTitle)
        }
       }

    }

    companion object{
        const val PAGE_SIZE=30
        const val STARTING_PAGE=1
    }
}