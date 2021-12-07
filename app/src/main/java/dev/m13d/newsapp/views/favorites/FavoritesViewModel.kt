package dev.m13d.newsapp.views.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.newsapp.db.NewsDao
import dev.m13d.newsapp.views.utils.AppState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritesViewModel(var db: NewsDao) : ViewModel() {

    private var favoriteNews: MutableLiveData<AppState> = MutableLiveData()
    private var disposable: CompositeDisposable = CompositeDisposable()
    fun getFavoriteNews(): LiveData<AppState> {
        return favoriteNews
    }

    fun getFavoriteNewsFromLocalStorage() {
        favoriteNews.value = AppState.Loading
        val subs = db.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { favoriteNews.postValue(AppState.Success(it)) }
        disposable.add(subs)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
