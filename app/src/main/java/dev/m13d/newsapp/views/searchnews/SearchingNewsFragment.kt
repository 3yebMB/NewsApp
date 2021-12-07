package dev.m13d.newsapp.views.searchnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.m13d.newsapp.R
import dev.m13d.newsapp.adapters.NewsListAdapter
import dev.m13d.newsapp.databinding.FragmentSearchingNewsBinding
import dev.m13d.newsapp.di.app.App
import dev.m13d.newsapp.model.entity.Article
import dev.m13d.newsapp.views.details.DetailsFragment
import dev.m13d.newsapp.views.factory.ViewModelFactory
import dev.m13d.newsapp.views.newslist.NewsListFragment
import dev.m13d.newsapp.views.utils.AppState
import dev.m13d.newsapp.views.utils.gone
import dev.m13d.newsapp.views.utils.visible
import javax.inject.Inject

class SearchingNewsFragment : Fragment() {

    private var _binding: FragmentSearchingNewsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SearchingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchingViewModel::class.java)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.appComponent.injectSearchNewsFragment(this)
        _binding = FragmentSearchingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun initRecyclerView() {
        adapter = NewsListAdapter(requireContext(), listener)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view?.findViewById(R.id.searchingNewsRecyclerView)!!
        initRecyclerView()
        viewModel.getNews().observe(viewLifecycleOwner) {
            renderData(it)
        }
        binding.searchText.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                viewModel.searchingData(it.toString())
            }
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.progress.gone()
                adapter.setData(appState.newsData)
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
