package com.digitalskies.androidpaging3.ui.mainactivity

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.digitalskies.androidpaging3.R
import com.digitalskies.androidpaging3.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private var job: Job?=null

    private lateinit var mainActivityViewModel: MainActivityViewModel

    @Inject lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory



    @Inject lateinit var newsArticlePagingAdapter: NewsArticlePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityViewModel=ViewModelProvider(this, mainActivityViewModelFactory)
                .get(MainActivityViewModel::class.java)
        val query:String=savedInstanceState?.getString(LAST_SEARCH)?: defaultSearch



        binding.searchView.setQuery(query,false)

        initAdapter()

        initRecyclerView()

        initRetryButton()

        initSearch()

        getSearchResults(query)


    }


    private fun initSearch() {
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {


                Log.d("MainActivity","progressBar is visible: ${binding.progressBar.isVisible}")


                query?.let {


                    getSearchResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }
    private fun getSearchResults(query:String) {

      /* mainActivityViewModel.totalResults.observe(this){
           it?.let {
           binding.tvTotalResults.text=getString(R.string.results, it) }

       }
*/
        job?.cancel()
        job=lifecycleScope.launch {
           binding.rvNewsArticles.visibility=View.GONE


           binding.tvTotalResults.visibility=View.INVISIBLE



            binding.progressBar.visibility= View.VISIBLE

            mainActivityViewModel.getArticles(query).collectLatest {

                binding.tvTotalResults.visibility=View.VISIBLE

                val totalResultsCount:Int?=mainActivityViewModel.getSearchResultsCount(query)
                totalResultsCount?.let {
                    binding.tvTotalResults.text=getString(R.string.results, it)
                    binding.tvTotalResults.visibility=View.VISIBLE
                }

                newsArticlePagingAdapter.submitData(it)



            }


        }

    }
    private fun initAdapter(){
        newsArticlePagingAdapter.addLoadStateListener { loadingState->
            binding.progressBar.isVisible=loadingState.mediator?.refresh is LoadState.Loading

            binding.btnRetry.isVisible=loadingState.mediator?.refresh is LoadState.Error


            val showResultsCount=loadingState.mediator?.refresh !is LoadState.Loading &&
                    loadingState.mediator?.refresh !is LoadState.Error

            binding.tvTotalResults.isVisible=showResultsCount

            val errorState=loadingState.mediator?.refresh is LoadState.Error && newsArticlePagingAdapter.itemCount==0

            binding.btnRetry.isVisible=errorState

            binding.rvNewsArticles.isVisible=!errorState


            val showNoResultsMessage=loadingState.mediator?.refresh is LoadState.NotLoading && newsArticlePagingAdapter.itemCount==0

            binding.tvMessage.isVisible=showNoResultsMessage || errorState

            if(showNoResultsMessage){
                binding.tvMessage.text=getString(R.string.no_results)
            }

            if(errorState){
                binding.tvMessage.text=getString(R.string.an_error_occured)
            }




        }
    }
    private fun initRecyclerView() {
        binding.rvNewsArticles.layoutManager=LinearLayoutManager(this)

       binding.rvNewsArticles.adapter=newsArticlePagingAdapter
               .withLoadStateHeaderAndFooter(
                       header = ArticleLoadingStateAdapter{newsArticlePagingAdapter.retry()},
                       footer = ArticleLoadingStateAdapter{newsArticlePagingAdapter.retry()}
               )

    }
    private fun initRetryButton() {
        binding.btnRetry.setOnClickListener{
            newsArticlePagingAdapter.retry()
        }
    }
    private fun toast(message:String) {
       Toast.makeText(this,message,Toast.LENGTH_LONG).show()

    }




    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(LAST_SEARCH,binding.searchView.query.toString())


    }

    companion object{
        val defaultSearch:String="US"
        private const val LAST_SEARCH="last_search_query"


    }
}