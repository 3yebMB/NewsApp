package dev.m13d.newsapp.views.newslist

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.newsapp.db.NewsDao
import dev.m13d.newsapp.model.api.RequestApi
import dev.m13d.newsapp.model.api.RetrofitClient
import dev.m13d.newsapp.views.utils.AppState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsViewModel @Inject constructor(var db: NewsDao, var requestApi: RequestApi) : ViewModel() {

    private var appState: MutableLiveData<AppState> = MutableLiveData()
    private var disposable: CompositeDisposable = CompositeDisposable()
    fun getAppState(): LiveData<AppState> {
        return appState
    }

    @SuppressLint("CheckResult")
    fun getNewsData() {

        val subscriber = requestApi
            .getNewsList("us", RetrofitClient.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { appState.postValue(AppState.Loading) }
            .subscribe(
                {
                    appState.postValue(AppState.Success(it.articles))
                    disposable.add(
                        db.deleteCache()
                            .subscribe({}, {})
                    )
                    for (item in it.articles) {
                        disposable.add(
                            db.insert(item)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe({}, {})
                        )
                    }
                },
                { _ ->
                    db.getAllNews().subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe {
                            appState.postValue(AppState.Success(it))
                        }
                })
        disposable.add(subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
