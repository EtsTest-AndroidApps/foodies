package com.moose.foodies.features.feature_home.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.mancj.materialsearchbar.MaterialSearchBar
import com.moose.foodies.R
import com.moose.foodies.databinding.ActivityHomeBinding
import com.moose.foodies.databinding.CarouselItemBinding
import com.moose.foodies.features.feature_favorites.presentation.FavoritesActivity
import com.moose.foodies.features.feature_home.domain.HomeData
import com.moose.foodies.features.feature_ingredients.presentation.IngredientsActivity
import com.moose.foodies.features.feature_recipe.presentation.RecipeActivity
import com.moose.foodies.features.feature_search.presentation.SearchActivity
import com.moose.foodies.util.ActivityHelper
import com.moose.foodies.util.extensions.*
import com.moose.foodies.util.onError
import com.moose.foodies.util.onSuccess
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        ActivityHelper.initialize(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.carousel.setImageHeight()

        binding.favoritesBtn.setOnClickListener { push<FavoritesActivity>() }
        binding.ingredientsBtn.setOnClickListener {push<IngredientsActivity>()}

        //Search bar config
        setSearchBar()

        // Get data
        viewModel.getLocalData()
        viewModel.getRemoteData()
        viewModel.data.observe(this, { result ->
            result.onSuccess {
                binding.motionLayout.transitionToState(R.id.loaded)
                bindData(it)
                viewModel.relaySuccess()
            }
            result.onError {
                if (it != "daily limit reached") showToast(it)
            }
        })

        setContentView(binding.root)
    }

    private fun bindData(data: HomeData) {
        with(binding){
            joke.text = data.joke
            trivia.text = data.trivia

            carousel.apply {
                size = data.recipes.size
                setCarouselViewListener { view, position ->
                    val recipe = data.recipes[position]
                    val url: String = recipe.info.image.largeImage()
                    val binding = CarouselItemBinding.bind(view)

                    binding.item.setImageHeight()
                    binding.recipeName.text = recipe.info.title
                    binding.recipeImage.load(url){
                        placeholder(R.drawable.loading)
                        transformations(RoundedCornersTransformation(topLeft = 10f, topRight = 10f))
                    }
                    view.setOnClickListener {
                        push<RecipeActivity> { it.putExtra("recipeId", recipe.id) }
                    }
                }
                show()
            }
        }
    }

    private fun setSearchBar() {
        binding.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) { hideBottomBar() }

            override fun onSearchStateChanged(enabled: Boolean) { hideBottomBar() }

            override fun onSearchConfirmed(text: CharSequence?) {
                push<SearchActivity> {
                    it.putExtra("query", text.toString())
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        ActivityHelper.initialize(this)
    }
}