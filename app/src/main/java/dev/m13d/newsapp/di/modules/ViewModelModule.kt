package dev.m13d.newsapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.m13d.newsapp.di.scope.ViewModelKey
import dev.m13d.newsapp.views.details.DetailsViewModel
import dev.m13d.newsapp.views.factory.ViewModelFactory
import dev.m13d.newsapp.views.favorites.FavoritesViewModel
import dev.m13d.newsapp.views.newslist.NewsViewModel
import dev.m13d.newsapp.views.searchnews.SearchingViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey( DetailsViewModel::class )
    abstract fun bindDetailsViewModel( mainViewModel: DetailsViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( SearchingViewModel::class )
    abstract fun bindSearchingViewModel( mainViewModel: SearchingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( NewsViewModel::class )
    abstract fun bindNewsViewModel( mainViewModel: NewsViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( FavoritesViewModel::class )

    abstract fun bindFavoritesViewModel( mainViewModel: FavoritesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory( factory: ViewModelFactory):
            ViewModelProvider.Factory
}
