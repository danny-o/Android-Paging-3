package com.digitalskies.androidpaging3.ui.mainactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.digitalskies.androidpaging3.R
import com.digitalskies.androidpaging3.databinding.NewsItemBinding
import com.digitalskies.androidpaging3.model.ArticleSearch.ArticleDetails
import javax.inject.Inject

class NewsArticlePagingAdapter @Inject constructor():PagingDataAdapter<ArticleDetails, ArticleViewHolder>(
    NEWS_ARTICLE_COMPARATOR) {
    private lateinit var binding:NewsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        binding= NewsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val articleDetails:ArticleDetails?=getItem(position)
        holder.articleUrl=articleDetails?.articleUrl

        holder.binding.tvArticleTitle.text=articleDetails?.title
        holder.binding.tvDescription.text=articleDetails?.description
        binding.tvSource.text=articleDetails?.source?.source

        val context=holder.binding.root.context

        val circularProgress=CircularProgressDrawable(context)
        circularProgress.strokeWidth=5f
        circularProgress.centerRadius=30f
        circularProgress.start()



        Glide.with(context)
            .load(articleDetails?.imageUrl)
            .apply(RequestOptions().placeholder(circularProgress).error(R.drawable.error))
            .into(holder.binding.articleImage)

    }

    companion object{

        private val NEWS_ARTICLE_COMPARATOR=object: DiffUtil.ItemCallback<ArticleDetails>(){
            override fun areItemsTheSame(oldItem: ArticleDetails, newItem: ArticleDetails): Boolean {
                return oldItem.title==newItem.title
            }

            override fun areContentsTheSame(oldItem: ArticleDetails, newItem: ArticleDetails): Boolean {
                return oldItem==newItem
            }


        }
    }

}