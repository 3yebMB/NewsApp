package dev.m13d.newsapp.views.searchnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.newsapp.model.api.RequestApi
import dev.m13d.newsapp.views.utils.AppState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchingViewModel(var requestApi: RequestApi) : ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private var appState: MutableLiveData<AppState> = MutableLiveData()

    fun getNews(): LiveData<AppState> {
        return appState
    }

    fun searchingData(request: String) {
        val subscriber = requestApi.searchForNews(request)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { appState.postValue(AppState.Loading) }
            .subscribe(
                {
                    appState.postValue(AppState.Success(it.articles))
                },
                {
                    it.printStackTrace()
                }
            )
        disposable.add(subscriber)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
