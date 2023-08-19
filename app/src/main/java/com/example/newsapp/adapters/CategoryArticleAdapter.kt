package com.example.newsapp.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.example.newsapp.utils.Constants

/** RECYCLER VIEW FOR CATEGORY NEWS
  This is responsible for displaying the list of news with title, image, etc.
  in Home page for the context of Category News **/
class CategoryArticleAdapter : RecyclerView.Adapter<ArticleCategoryHolder>() {
    var newsCategoryList = listOf<ArticleRequest>()
    private var itemListener : ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleCategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_category_item_list, parent, false)
        return ArticleCategoryHolder(view)
    }
    override fun onBindViewHolder(holder: ArticleCategoryHolder, position: Int) {

        val article = newsCategoryList[position]
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

        holder.itemView.setOnClickListener {
            itemListener?.onItemClicked(position, article)
        }
    }
    override fun getItemCount(): Int {
        return newsCategoryList.size
    }

    fun setItemCategoryClickListener(itemClickListener : ItemClickListener){
        this.itemListener = itemClickListener

    }
    @SuppressLint("NotifyDataSetChanged")
    fun setCategorylist(articles: List<ArticleRequest>) {
        this.newsCategoryList = articles
        notifyDataSetChanged()
    }

}
interface ItemClickListener {
    fun onItemClicked(position: Int, articleRequest: ArticleRequest)
}



class ArticleCategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val textTitle : TextView = itemView.findViewById(R.id.tvCategoryTitle)
    val tvSource : TextView = itemView.findViewById(R.id.tvCategorySource)
    val tvPubslishedAt : TextView = itemView.findViewById(R.id.tvCategoryPublishedAt)
    val imageView : ImageView = itemView.findViewById(R.id.ivArticleCategoryImage)
}