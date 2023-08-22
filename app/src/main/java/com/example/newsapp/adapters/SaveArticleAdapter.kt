package com.example.newsapp.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.newsapp.R
import com.example.newsapp.models.ArticleRequest
import com.example.newsapp.models.SavedArticle
import com.example.newsapp.utils.Constants

class SaveArticleAdapter : RecyclerView.Adapter<SavedArticleViewHolder>() {
    var newsSaveArticleList = listOf<SavedArticle>()


    private val differCallback = object : DiffUtil.ItemCallback<SavedArticle>() {
        override fun areItemsTheSame(oldItem: SavedArticle, newItem: SavedArticle): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: SavedArticle, newItem: SavedArticle): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_category_item_list, parent, false)
        return SavedArticleViewHolder(view)
    }
    override fun onBindViewHolder(holder: SavedArticleViewHolder, position: Int) {

        val article = newsSaveArticleList[position]
        val requestOption = RequestOptions()

        holder.itemView.apply {
            // FOR LOADING OF IMAGE
            Glide.with(this).load(article.urlToImage).apply(requestOption).listener(object:
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView)
            holder.textTitle.setText(article.title)
            holder.tvSource.setText(article.source!!.name)
            holder.tvPubslishedAt.setText(Constants.DateFormat(article.publishedAt))
        }
    }
    override fun getItemCount(): Int {
        return newsSaveArticleList.size
    }

    @SuppressLint("NotifyItemInserted")
    fun setSavedList(articles: List<SavedArticle>) {
        this.newsSaveArticleList = articles
        notifyItemInserted(newsSaveArticleList.size-1)
    }

}
class SavedArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val textTitle : TextView = itemView.findViewById(R.id.tvCategoryTitle)
    val tvSource : TextView = itemView.findViewById(R.id.tvCategorySource)
    val tvPubslishedAt : TextView = itemView.findViewById(R.id.tvCategoryPublishedAt)
    val imageView : ImageView = itemView.findViewById(R.id.ivArticleCategoryImage)
}