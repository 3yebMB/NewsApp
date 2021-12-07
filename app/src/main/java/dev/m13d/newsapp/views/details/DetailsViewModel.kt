package dev.m13d.newsapp.views.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.newsapp.db.NewsDao
import dev.m13d.newsapp.model.entity.Article
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(var db: NewsDao) : ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()
    var checkPoint: MutableLiveData<Boolean> = MutableLiveData()
    fun saveArticleInLocalStorage(article: Article?) {
        disposable.add(db.insert(article)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ checkPoint.postValue(true) },
                {
                    it.printStackTrace()
                }
            )
        )
    }

    fun getCheckPoint(): LiveData<Boolean> {
        return checkPoint
    }

    fun checkNewsInFavorite(article: Article) {
        val subs = db.getNews(article.title)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                checkPoint.postValue(true)
            }
        disposable.add(subs)
    }

    fun deleteArticleFromLocalStorage(article: Article) {
        disposable.add(
            db.deleteNews(article.title)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doAfterSuccess {
                    checkPoint.postValue(false)
                }.subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
