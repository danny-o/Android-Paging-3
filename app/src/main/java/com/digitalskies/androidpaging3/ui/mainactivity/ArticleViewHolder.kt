package com.digitalskies.androidpaging3.ui.mainactivity


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.digitalskies.androidpaging3.databinding.NewsItemBinding
import com.digitalskies.androidpaging3.ui.newsArticleActivity.ARTICLE_URL
import com.digitalskies.androidpaging3.ui.newsArticleActivity.NewsArticleActivity

class ArticleViewHolder(var binding: NewsItemBinding):RecyclerView.ViewHolder(binding.root){
        var articleUrl:String?=null

        val context: Context? =binding.root.context

    init {
        binding.root.setOnClickListener {
            var intent= Intent(context,NewsArticleActivity::class.java)

            intent.putExtra(ARTICLE_URL,articleUrl)

            context?.startActivity(intent)
        }
    }


}