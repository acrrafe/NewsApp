package com.example.newsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapters.SaveArticleAdapter
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac

class savedNewsFragment : Fragment() {

    private var _binding : FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel : NewsViewModel
    lateinit var newsSaveArticleAdapter : SaveArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepository(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]


        viewModel.getSavedNews.observe(viewLifecycleOwner, Observer {
            setUpCategoryRecyclerView()
            newsSaveArticleAdapter.setSavedList(it)
        })
    }
    private fun setUpCategoryRecyclerView() {
        newsSaveArticleAdapter = SaveArticleAdapter()
        binding.saveRecNews.apply {
            adapter = newsSaveArticleAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

}