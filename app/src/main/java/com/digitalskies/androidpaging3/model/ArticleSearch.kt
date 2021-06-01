package com.digitalskies.androidpaging3.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article_search")
data class ArticleSearch @Ignore constructor(
    @SerializedName("articles") @Ignore var articles:List<ArticleDetails>?=null,
    @SerializedName("totalResults")val totalResults:Int,
    @PrimaryKey        var search:String,
){
        constructor(totalResults: Int, search: String) : this(null,totalResults,search)

    @Entity(tableName = "article_details")
    data class ArticleDetails(
        @Embedded @SerializedName ("source")
        var source:Source?,
        @SerializedName("title")
        @PrimaryKey
        var title:String,
        @SerializedName("description")
        var description:String?,
        @SerializedName("url")
        var articleUrl:String?,
        @SerializedName("urlToImage")
        var imageUrl:String?,
        var search:String,
        var prevKey:Int?,
        var nextKey:Int?



    )
    data class Source(@SerializedName("id") var SourceId:String?, @SerializedName("name") var source:String?)


}
data class ArticleSearchAndArticles(
    @Embedded val articleSearch: ArticleSearch,
    @Relation(parentColumn = "search",entityColumn = "search")
    val articles: List<ArticleSearch.ArticleDetails>
)

