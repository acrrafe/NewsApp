package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleAdapter
import com.example.newsapp.adapters.CategoryArticleAdapter
import com.example.newsapp.adapters.ItemClickListener
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.databinding.FragmentDiscoveryBinding
import com.example.newsapp.models.ArticleRequest
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.utils.NetworkResult
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac

class fragmentDiscovery : Fragment(), ItemClickListener {

    private var _binding : FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel : NewsViewModel
    lateinit  var newsAdapter : ArticleAdapter
    lateinit  var newsCategoryAdapter : CategoryArticleAdapter
    var addingResponselist = arrayListOf<ArticleRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepository(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        setUpCategoryRecyclerView()
        bindCategoryObservers()


        binding.searchNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchNews.clearFocus()

                viewModel.getCategory(query!!)
                setUpCategoryRecyclerView()
                bindCategoryObservers()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setUpCategoryRecyclerView() {
        newsCategoryAdapter = CategoryArticleAdapter()
        newsCategoryAdapter.setItemCategoryClickListener(this)

        binding.discRecNews.apply {
            adapter = newsCategoryAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun bindCategoryObservers() {
        viewModel.categoryResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it){
                is NetworkResult.Success->{
                    binding.paginationProgressBarDisc.visibility = View.GONE
                    binding.discRecNews.visibility = View.VISIBLE
                    it.data?.let{newsresponse->
                        addingResponselist = newsresponse.articles as ArrayList<ArticleRequest>
                        newsCategoryAdapter.setCategorylist(newsresponse.articles)
                    }
                }
                is NetworkResult.Error->{
                    binding.paginationProgressBarDisc.visibility = View.GONE
                    it.message?.let{messsage->
                        Log.i("BREAKING FRAG", messsage.toString())
                    }
                }
                is NetworkResult.Loading->{
                    binding.paginationProgressBarDisc.visibility = View.VISIBLE
                    binding.discRecNews.visibility = View.INVISIBLE
                }
            }
        })
    }

    override fun onItemClicked(position: Int, articleRequest: ArticleRequest) {
        val action = fragmentDiscoveryDirections.actionFragmentDiscoveryToArticleFragment(articleRequest)
        view?.findNavController()?.navigate(action)
    }

}