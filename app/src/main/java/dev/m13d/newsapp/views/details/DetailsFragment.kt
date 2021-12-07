package dev.m13d.newsapp.views.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dev.m13d.newsapp.R
import dev.m13d.newsapp.databinding.FragmentDetailsBinding
import dev.m13d.newsapp.di.app.App
import dev.m13d.newsapp.model.entity.Article
import dev.m13d.newsapp.views.factory.ViewModelFactory
import dev.m13d.newsapp.views.utils.gone
import javax.inject.Inject

class DetailsFragment : Fragment() {

    private var flag: Boolean = false
    private var article: Article? = null
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: DetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            article = it.getParcelable(BUNDLE_EXTRA)
            article?.type = 1
        }
        App.appComponent.injectDetailsFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataInViews()
        viewModel.checkNewsInFavorite(article!!)
        viewModel.getCheckPoint().observe(viewLifecycleOwner, {
            flag = it
            if (it) {
                binding.like.setImageResource(R.drawable.unlike)
            } else if (!it) {
                binding.like.setImageResource(R.drawable.ic_favorite)
            }
        })
        binding.like.setOnClickListener {
            if (!flag) {
                viewModel.saveArticleInLocalStorage(article)
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_LONG).show()
            } else if (flag) {
                viewModel.deleteArticleFromLocalStorage(article!!)
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setDataInViews() {
        binding.author.text = article?.author
        binding.title.text = article?.title
        binding.desc.text = article?.description
        binding.publishedAt.text = article?.publishedAt?.substring(0, 10)
        Picasso.with(requireContext()).load(article?.urlToImage).into(
            binding.img,
            object : Callback {
                override fun onSuccess() {
                    binding.progressBar.gone()
                }
                override fun onError() {
                    binding.progressBar.gone()
                }
            })
    }

    companion object {
        const val BUNDLE_EXTRA = "news"

        @JvmStatic
        fun newInstance(article: Article) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_EXTRA, article)
                }
            }
    }
}
