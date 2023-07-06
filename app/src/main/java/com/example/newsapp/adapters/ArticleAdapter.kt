package com.example.newsapp.adapters

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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

class ArticleAdapter() : RecyclerView.Adapter<ArticleHolder>() {
    var newsList = listOf<ArticleRequest>()
    private var itemListener : ItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item_list, parent, false)
        return ArticleHolder(view)
    }
    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {

        val article = newsList[position]

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
                    holder.pb.visibility = View.VISIBLE
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pb.visibility = View.GONE
                    return false
                }

            }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView)

            holder.textTitle.setText(article.title)
            holder.tvSource.setText(article.source!!.name)
            holder.tvDescription.setText(article.description)
            holder.tvPubslishedAt.setText(Constants.DateFormat(article.publishedAt))
        }
        // storing the position and articles in the click event
        // in case we go to another framgnet
        holder.itemView.setOnClickListener {
            itemListener?.onItemClicked(position, article)
        }
    }
    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setItemClickListener(itemListener : ItemListener){
        this.itemListener = itemListener

    }
    @SuppressLint("NotifyDataSetChanged")
    fun setlist(articles: List<ArticleRequest>) {
        this.newsList = articles
        notifyDataSetChanged()
    }

}
interface ItemListener {
    fun onItemClicked(position: Int, articleRequest: ArticleRequest)
}





class ArticleHolder(itemView: View) : ViewHolder(itemView){
    val textTitle : TextView = itemView.findViewById(R.id.tvTitle)
    val tvSource : TextView = itemView.findViewById(R.id.tvSource)
    val tvDescription : TextView = itemView.findViewById(R.id.tvDescription)
    val tvPubslishedAt : TextView = itemView.findViewById(R.id.tvPublishedAt)
    val imageView : ImageView = itemView.findViewById(R.id.ivArticleImage)
    val pb : ProgressBar = itemView.findViewById(R.id.pbImage)



}