package dev.m13d.newsapp.di.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dev.m13d.newsapp.di.modules.AppModule
import dev.m13d.newsapp.di.modules.LocalModule
import dev.m13d.newsapp.di.modules.RestModule
import dev.m13d.newsapp.di.modules.ViewModelModule
import dev.m13d.newsapp.views.details.DetailsFragment
import dev.m13d.newsapp.views.favorites.FavoritesNewsFragment
import dev.m13d.newsapp.views.newslist.NewsListFragment
import dev.m13d.newsapp.views.searchnews.SearchingNewsFragment
import javax.inject.Singleton

@Component(modules = [AppModule::class, RestModule::class, LocalModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {
    fun injectNewsListFragment(newsListFragment: NewsListFragment)
    fun injectFavoritesFragment(favoriteNewsFragment: FavoritesNewsFragment)
    fun injectSearchNewsFragment(searchingNewsFragment: SearchingNewsFragment)
    fun injectDetailsFragment(detailsNewsFragment: DetailsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}
