package com.digitalskies.androidpaging3.data
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.digitalskies.androidpaging3.model.ArticleSearch
import com.digitalskies.androidpaging3.model.ArticleSearch.ArticleDetails
import com.digitalskies.androidpaging3.model.ArticleSearchAndArticles
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Transaction
    @Query("SELECT totalResults FROM article_search WHERE search=:search")
    suspend fun getSearchResultsCount(search:String): Int?


    @Query("SELECT * FROM article_details WHERE search like :search")
    fun getArticles(search:String): PagingSource<Int,ArticleDetails>


    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = ArticleSearch::class)
    suspend fun insertArticleSearch(articleSearch: ArticleSearch)

    @Insert(onConflict = OnConflictStrategy.REPLACE,entity = ArticleDetails::class)
    suspend fun insertArticles(articles:List<ArticleDetails>)

    @Query("DELETE FROM article_details")
    suspend fun deleteArticles()

    @Query("DELETE FROM article_search")
    suspend fun deleteSearch()
}