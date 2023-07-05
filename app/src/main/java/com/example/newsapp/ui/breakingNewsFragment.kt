package com.example.newsapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleAdapter
import com.example.newsapp.adapters.ItemListener
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.models.ArticleRequest
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.utils.NetworkResult
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac

class breakingNewsFragment : Fragment(), ItemListener{


    lateinit var viewModel : NewsViewModel
    lateinit  var newsAdapter : ArticleAdapter

    private var _binding : FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    var addingResponselist = arrayListOf<ArticleRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepository(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo !=null && netInfo.isConnected){
            setUpRecyclerView()
            bindObservers()
        }

    }
    private fun bindObservers() {
        viewModel.articleResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it){
                is NetworkResult.Success->{
                    hideProgressBar()
                    it.data?.let{newsresponse->
                        addingResponselist = newsresponse.articles as ArrayList<ArticleRequest>
                        newsAdapter.setlist(newsresponse.articles)
                    }
                }
                is NetworkResult.Error->{
                    hideProgressBar()
                    it.message?.let{messsage->
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
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
    fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun setUpRecyclerView() {
        newsAdapter = ArticleAdapter()
        newsAdapter.setItemClickListener(this)
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    override fun onItemClicked(position: Int, articleRequest: ArticleRequest) {
        // TODO: Implement Navigator Controll
    }
}