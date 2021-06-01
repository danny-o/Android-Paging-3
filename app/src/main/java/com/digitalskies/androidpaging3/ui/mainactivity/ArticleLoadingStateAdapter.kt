package com.digitalskies.androidpaging3.ui.mainactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.digitalskies.androidpaging3.databinding.LoadingStateItemBinding

class ArticleLoadingStateAdapter(private val retry:()->Unit):LoadStateAdapter<ArticleLoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {

        if(loadState is LoadState.Error){

            holder.textError.text=loadState.error.localizedMessage
            holder.textError.isVisible=true
        }

       holder.btnProgressBar.isVisible=loadState is LoadState.Loading

       holder.retryBtn.isVisible=loadState is LoadState.Error

       holder.textError.isVisible= loadState is LoadState.Error


    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {

        val binding=LoadingStateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadingStateViewHolder(retry,binding)
    }



    class LoadingStateViewHolder(retry: () -> Unit,binding: LoadingStateItemBinding):RecyclerView.ViewHolder(binding.root){
        var btnProgressBar=binding.progressBar2
        var retryBtn=binding.btnRetry
        var textError=binding.tvError
        init {

            binding.btnRetry.setOnClickListener{
                retry.invoke()
            }
        }
    }


}