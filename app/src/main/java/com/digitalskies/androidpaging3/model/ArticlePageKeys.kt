package com.digitalskies.androidpaging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_keys")
data class ArticlePageKeys(
    var prevKey:Int?,
    var nextKey:Int?,
    @PrimaryKey var articleTitle:String
) {
}