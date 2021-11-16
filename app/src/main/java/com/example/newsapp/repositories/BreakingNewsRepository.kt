package com.example.newsapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.data.local.room.ArticlesDao
import com.example.newsapp.data.remote.RetroInstance
import com.example.newsapp.model.Article
import com.example.newsapp.model.Constants.Companion.API_KEY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BreakingNewsRepository (val db: ArticlesDao) {
    val articlesList = MutableLiveData<List<Article>>()
    var disposable: Disposable? = null

    fun getAllArticles(): MutableLiveData<List<Article>> {
        disposable = RetroInstance.api.getBreakingNews("us", "business", API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                articlesList.value = it.articles
            }

        return articlesList
    }

    fun getSearchArticles(query: String): MutableLiveData<List<Article>> {
        disposable = RetroInstance.api.searchNews(API_KEY, query, "popularity")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                articlesList.value = it.articles
            }
        return articlesList
    }
    fun getOfflineArticles():LiveData<List<Article>>{
        return db.articlesList()
    }

     fun insertArticle(article: Article) {
        db.insertArticle(article)
    }
}