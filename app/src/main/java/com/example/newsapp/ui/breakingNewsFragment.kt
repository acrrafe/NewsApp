package com.example.newsapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleAdapter
import com.example.newsapp.adapters.CategoryArticleAdapter
import com.example.newsapp.adapters.ItemClickListener
import com.example.newsapp.adapters.ItemListener
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.models.ArticleRequest
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.utils.NetworkResult
import com.example.newsapp.utils.SharedPreferenceInstance
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac

class breakingNewsFragment : Fragment(), ItemListener, ItemClickListener{

    private var _binding : FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel : NewsViewModel
    lateinit  var newsAdapter : ArticleAdapter
    lateinit  var newsCategoryAdapter : CategoryArticleAdapter
    var addingResponselist = arrayListOf<ArticleRequest>()

    private var isClicked: Boolean? = null
    private lateinit var sharedPref: SharedPreferenceInstance
    private var isNightMode: Boolean = false
    private lateinit var settingCountry: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPreferenceInstance.getInstance(requireContext().applicationContext)

        sharedPref.let {
            isNightMode = it.getBoolean("night", false)
            settingCountry = it.getString("country", "us")
            if(isNightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepository(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

            viewModel.getBreakingNews(settingCountry)
            setUpBreakinngRecyclerView()
            setUpCategoryRecyclerView()
            bindBreakingObservers()
            bindCategoryObservers()

        binding.businessBtn.setOnClickListener{
            isClicked = true
            viewModel.getCountryWCategory(settingCountry, "business")
            setUpCategoryRecyclerView()
            bindCategoryObservers()
        }
        binding.sportBtn.setOnClickListener{
            isClicked = true
            viewModel.getCountryWCategory(settingCountry,"sports")
            setUpCategoryRecyclerView()
            bindCategoryObservers()
        }
        binding.techBtn.setOnClickListener{
            isClicked = true
            viewModel.getCountryWCategory(settingCountry,"technology")
            setUpCategoryRecyclerView()
            bindCategoryObservers()
        }
        binding.educBtn.setOnClickListener{
            isClicked = true
            viewModel.getCountryWCategory(settingCountry,"education")
            setUpCategoryRecyclerView()
            bindCategoryObservers()
        }
        binding.politicsBtn.setOnClickListener{
            isClicked = true
            viewModel.getCountryWCategory(settingCountry,"politics")
            setUpCategoryRecyclerView()
            bindCategoryObservers()
        }
        binding.cryptoBtn.setOnClickListener{
            isClicked = true
            viewModel.getCountryWCategory(settingCountry,"crypto")
            setUpCategoryRecyclerView()
            bindCategoryObservers()
        }

    }

    // UPDATE THE BREAKING NEWS LIVE DATA IN VIEW MODEL
    private fun bindBreakingObservers() {
        viewModel.breakingNewsResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it){
                is NetworkResult.Success->{
                    binding.paginationProgressBar.visibility = View.GONE
                    binding.parentLayout.visibility = View.VISIBLE
                    it.data?.let{newsresponse->
                        addingResponselist = newsresponse.articles as ArrayList<ArticleRequest>
                        newsAdapter.setlist(newsresponse.articles)
                    }
                }
                is NetworkResult.Error->{
                    binding.paginationProgressBar.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    it.message?.let{message->
                        binding.errMessage.text = message
                    }
                }
                is NetworkResult.Loading->{
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
            }
        })
    }
    // UPDATE THE CATEGORY NEWS LIVE DATA IN VIEW MODEL
    private fun bindCategoryObservers() {
        viewModel.countryWCategoryResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it){
                is NetworkResult.Success->{
                    binding.paginationProgressBarCateg.visibility = View.INVISIBLE
                    binding.rvCategoryNews.visibility = View.VISIBLE
                    it.data?.let{newsresponse->
                        addingResponselist = newsresponse.articles as ArrayList<ArticleRequest>
                        newsCategoryAdapter.setCategorylist(newsresponse.articles)
                    }
                }
                is NetworkResult.Error->{
                    binding.paginationProgressBarCateg.visibility = View.INVISIBLE
                    it.message?.let{messsage->
                        Log.i("BREAKING FRAG", messsage.toString())
                    }
                }
                is NetworkResult.Loading->{
                    binding.paginationProgressBarCateg.visibility = View.VISIBLE
                    binding.rvCategoryNews.visibility = View.INVISIBLE
                }
            }
        })
    }
    private fun setUpBreakinngRecyclerView() {
        newsAdapter = ArticleAdapter()
        newsAdapter.setItemClickListener(this)
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

    }
    private fun setUpCategoryRecyclerView() {
        newsCategoryAdapter = CategoryArticleAdapter()
        newsCategoryAdapter.setItemCategoryClickListener(this)

        binding.rvCategoryNews.apply {
            adapter = newsCategoryAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
    override fun onItemClicked(position: Int, articleRequest: ArticleRequest) {
        val action = breakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(articleRequest)
        view?.findNavController()?.navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}