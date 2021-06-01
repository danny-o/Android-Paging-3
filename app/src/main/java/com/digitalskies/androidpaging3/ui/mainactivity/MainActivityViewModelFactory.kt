package com.digitalskies.androidpaging3.ui.mainactivity


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.digitalskies.androidpaging3.repository.NewsRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject


class MainActivityViewModelFactory @Inject constructor(var application: Application, var newsRepository: NewsRepository):ViewModelProvider.Factory {

    override fun <T: ViewModel?> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){

            return MainActivityViewModel(application,newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}