package dev.m13d.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dev.m13d.newsapp.R
import dev.m13d.newsapp.model.entity.Article
import dev.m13d.newsapp.views.newslist.NewsListFragment
import dev.m13d.newsapp.views.utils.DiffUtilCallback
import dev.m13d.newsapp.views.utils.gone
import kotlinx.android.synthetic.main.news_list_itemm.view.*

class NewsListAdapter(
    private val context: Context,
    private val onItemViewClickListener: NewsListFragment.OnItemViewClickListener,
    private var news: List<Article> = ArrayList()
) : RecyclerView.Adapter<NewsListAdapter.MyViewHolder>() {

    private lateinit var mDiffResult: DiffUtil.DiffResult
    private var prevNewsList: List<Article> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.news_list_itemm, null, false)
        return MyViewHolder(view)
    }

    fun setData(list: List<Article>) {

        news = list
        mDiffResult = DiffUtil.calculateDiff(DiffUtilCallback(prevNewsList, list))
        mDiffResult.dispatchUpdatesTo(this)
        prevNewsList = list
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(news[position])
        Picasso.with(context).load(news[position].urlToImage)
            .fit()
            .into(holder.newsImage, object : Callback {
                override fun onSuccess() {
                    holder.progress.gone()
                }

                override fun onError() {
                    holder.progress.gone()
                }
            })
    }

    override fun getItemCount(): Int {
        return news.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.title
        var progress: ProgressBar = itemView.item_progress_bar
        var description: TextView = itemView.desc
        var author: TextView = itemView.author
        var newsImage: ImageView = itemView.img
        var publishedAt: TextView = itemView.publishedAt

        fun bind(news: Article) {
            title.text = news.title
            publishedAt.text = news.publishedAt.substring(0, 10)
            description.text = news.description
            author.text = news.author
            title.text = news.title
            itemView.setOnClickListener {
                onItemViewClickListener.onItemViewClick(news)
            }
        }
    }
}
