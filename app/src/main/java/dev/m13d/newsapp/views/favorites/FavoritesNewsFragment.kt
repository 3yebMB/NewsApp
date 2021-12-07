package dev.m13d.newsapp.views.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.m13d.newsapp.R
import dev.m13d.newsapp.adapters.NewsListAdapter
import dev.m13d.newsapp.databinding.FragmentFavoritesNewsBinding
import dev.m13d.newsapp.di.app.App
import dev.m13d.newsapp.model.entity.Article
import dev.m13d.newsapp.views.details.DetailsFragment
import dev.m13d.newsapp.views.factory.ViewModelFactory
import dev.m13d.newsapp.views.newslist.NewsListFragment
import dev.m13d.newsapp.views.utils.AppState
import dev.m13d.newsapp.views.utils.gone
import dev.m13d.newsapp.views.utils.visible
import javax.inject.Inject

class FavoritesNewsFragment : Fragment() {

    private var _binding: FragmentFavoritesNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: FavoritesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val listener = object : NewsListFragment.OnItemViewClickListener {
        override fun onItemViewClick(article: Article) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, article)
                findNavController().navigate(R.id.detailsFragment, bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.injectFavoritesFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavoritesViewModel::class.java)
        viewModel.getFavoriteNewsFromLocalStorage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById(R.id.newsRecyclerView)!!
        viewModel.getFavoriteNews().observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    fun initRecyclerView(news: List<Article>) {
        adapter = NewsListAdapter(requireContext(), listener, news)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.progress.gone()
                initRecyclerView(appState.newsData)
            }
            is AppState.Loading -> {
                binding.progress.visible()
            }
            is AppState.Error -> {
                binding.progress.gone()
            }
        }
    }
}
