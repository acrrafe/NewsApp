package com.example.newsapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.SaveArticleAdapter
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.utils.SwipeToDeleteCallback
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac
import com.google.android.material.snackbar.Snackbar


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

        val itemTouchHelperCallback = object : SwipeToDeleteCallback(requireActivity().applicationContext) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val savedArticle = newsSaveArticleAdapter.newsSaveArticleList[position]
                viewModel.deleteArticle(savedArticle)
                Snackbar.make(view, "Successfully Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.insertArticle(savedArticle)
                    }
                    setActionTextColor(Color.WHITE)
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.saveRecNews)
        }

    }
    private fun setUpCategoryRecyclerView() {
        newsSaveArticleAdapter = SaveArticleAdapter()
        binding.saveRecNews.apply {
            adapter = newsSaveArticleAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

}