package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var adapter: NewsAdapter
    private val articles = mutableListOf<Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = NewsAdapter(this@MainActivity, articles)
        binding.newsList.adapter = adapter
//        binding.newsList.layoutManager = LinearLayoutManager(this@MainActivity)

        val layoutManager = StackLayoutManager()
        binding.newsList.layoutManager = layoutManager
        getNews()

    }

    private fun getNews() {
        val news = NewsService.newsInstance.getHeadlines("us",1)
        news.enqueue(object: Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news:News? = response.body()
                if(news != null){
                    Log.d("newsaa", news.toString())
                    articles.addAll(news.articles)
                    articles.removeIf({article -> article.title == null})
                    adapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("AA", "Error")
            }
        })
    }
}