package com.example.newsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.models.SavedArticle
import com.example.newsapp.models.Source
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.utils.Constants
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.NewsViewModelFac
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat

class articleFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var args : articleFragmentArgs

    private var _binding : FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private var stringCheck : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepository(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        args = articleFragmentArgs.fromBundle(requireArguments())

        val source = Source(args.article.source!!.id, args.article.source!!.name)

        binding.tvTitle.text = args.article.title
        binding.tvSource.text = args.article.source!!.name
        binding.tvDescription.text = args.article.description
        binding.tvPublishedAt.text = Constants.DateFormat(args.article.publishedAt)
        Glide.with(requireActivity()).load(args.article.urlToImage).into(binding.articleImage)

        viewModel.getSavedNews.observe(viewLifecycleOwner, Observer {
            for(items in it){
                stringCheck = items.title
            }
        })
        binding.toolbar.saveIcon.setOnClickListener {
            viewModel.insertArticle(SavedArticle(0, args.article.description!!, args.article.publishedAt!!,
                args.article.source!!, args.article.title!!, args.article.url!!, args.article.urlToImage!!))
            Snackbar.make(view, "News Saved Successfully!", Snackbar.LENGTH_SHORT).show()
        }

        binding.toolbar.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}