package com.digitalskies.androidpaging3.network

import com.digitalskies.androidpaging3.model.ArticleSearch
import retrofit2.http.*

interface NewsApi {


    @GET("{everything}")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun getNewsArticles(@Path("everything")everything:String="everything",
                                @Query("q")query:String,
                                @Query("apiKey")apiKey:String,
                                @Query("pageSize")pageSize:Int,
                                @Query("page")page:Int):ArticleSearch
}