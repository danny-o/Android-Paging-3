package com.digitalskies.androidpaging3.data

import androidx.room.*
import com.digitalskies.androidpaging3.model.ArticlePageKeys

@Dao
interface KeysDao {

 @Query("SELECT * FROM page_keys WHERE articleTitle=:title")
 suspend fun getArticleKeysByTitle(title:String):ArticlePageKeys

 @Query("SELECT * FROM page_keys")
 suspend fun getAllKeys():List<ArticlePageKeys>

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertPageKeys(articlePageKeys: List<ArticlePageKeys>)

 @Query("DELETE FROM page_keys")
 suspend fun deletePageKeys()


}