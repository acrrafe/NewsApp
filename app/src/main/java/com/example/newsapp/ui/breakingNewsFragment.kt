package com.example.newsapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleAdapter
import com.example.newsapp.adapters.ItemListener
import com.example.newsapp.models.ArticleRequest
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.utils.NetworkResult
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac

class breakingNewsFragment : Fragment(), ItemListener{


    lateinit var viewModel : NewsViewModel
    lateinit  var newsAdapter : ArticleAdapter
    lateinit var rv : RecyclerView
    lateinit var pb : ProgressBar
    var addingResponselist = arrayListOf<ArticleRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepository(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]


        rv = view.findViewById(R.id.rvBreakingNews)
        pb = view.findViewById(R.id.paginationProgressBar)

        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        if (nInfo !=null && nInfo.isConnected){

            setUpRecyclerView()
            loadBreakingNews()



        }
    }

    private fun loadCategoryNews() {

        viewModel._articleResponseLiveData.observe(viewLifecycleOwner, Observer {response->
            when (response){
                is NetworkResult.Success->{
                    hideProgressBar()
                    response.data?.let{newsresponse->
                        addingResponselist = newsresponse.articles as ArrayList<ArticleRequest>
                        newsAdapter.setlist(newsresponse.articles)
                    }
                }
                is NetworkResult.Error->{
                    hideProgressBar()
                    response.message?.let{messsage->
                        Log.i("BREAKING FRAG", messsage.toString())

                    }
                }
                is NetworkResult.Loading->{
                    showProgressBar()
                }

            }
        })
    }

    private fun loadBreakingNews() {

        viewModel._articleResponseLiveData.observe(viewLifecycleOwner, Observer {response->
            when (response){
                is NetworkResult.Success->{
                    hideProgressBar()
                    response.data?.let{newsresponse->
                        addingResponselist = newsresponse.articles as ArrayList<ArticleRequest>
                        newsAdapter.setlist(newsresponse.articles)
                    }
                }
                is NetworkResult.Error->{
                    hideProgressBar()
                    response.message?.let{messsage->
                        Log.i("BREAKING FRAG", messsage.toString())

                    }
                }
                is NetworkResult.Loading->{
                    showProgressBar()
                }

            }
        })
    }

    fun showProgressBar(){
        pb.visibility = View.VISIBLE
    }
    fun hideProgressBar(){
        pb.visibility = View.INVISIBLE
    }
    private fun setUpRecyclerView() {
        newsAdapter = ArticleAdapter()
        newsAdapter.setItemClickListener(this)
        rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    override fun onItemClicked(position: Int, articleRequest: ArticleRequest) {
        // TODO: Implement Navigator Controll
    }
}