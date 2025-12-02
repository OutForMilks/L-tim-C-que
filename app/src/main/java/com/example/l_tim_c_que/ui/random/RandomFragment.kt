package com.example.l_tim_c_que.ui.random

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.api.APIClient
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.firebase.FirebaseDB
import com.example.l_tim_c_que.repository.MealRepository
import com.example.l_tim_c_que.viewmodel.BookmarkViewModel
import com.example.l_tim_c_que.viewmodel.BookmarkViewModelFactory
import com.example.l_tim_c_que.viewmodel.RandomViewModel
import com.example.l_tim_c_que.viewmodel.RandomViewModelFactory
import com.example.l_tim_c_que.viewmodel.MealDetailViewModel
import com.example.l_tim_c_que.viewmodel.MealDetailViewModelFactory
import kotlin.getValue

/**
 * Fragment responsible for displaying the user's recently viewed meals.
 *
 * This fragment is currently a placeholder and inflates the `fragment_recent` layout.
 * Future implementation should include fetching and displaying the recent history from a local database or repository.
 */
class RandomFragment : Fragment() {

    internal val mealDetailViewModel: MealDetailViewModel by activityViewModels {
        MealDetailViewModelFactory(MealRepository(APIClient.api))
    }

    internal val bookmarkViewModel: BookmarkViewModel by activityViewModels {
        BookmarkViewModelFactory(MealRepository(APIClient.api))
    }

    internal val randomViewModel: RandomViewModel by activityViewModels {
        RandomViewModelFactory(MealRepository(APIClient.api))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        randomViewModel.searchMealByRandom()

        return inflater.inflate(R.layout.fragment_meal_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupObservers(view)
    }
}

/**
 * Initializes the views and sets up initial state if needed.
 * Currently acts as a holder for view references used in observers.
 * @param view The root view of the fragment.
 */
private fun RandomFragment.setupViews(view: View) {
    // No specific initial setup needed beyond findViewById which is done in update methods or observers
    // But we can keep it for structure or future initializations
}

/**
 * Sets up observers for ViewModels to update the UI when data changes.
 * @param view The root view of the fragment.
 */
private fun RandomFragment.setupObservers(view: View) {
    val banner = view.findViewById<ImageView>(R.id.recipe_banner)
    val title = view.findViewById<TextView>(R.id.recipe_title)
    val country = view.findViewById<TextView>(R.id.recipe_country)
    val bookmark = view.findViewById<Button>(R.id.bookmark_button) // Unused currently
    val ingredients = view.findViewById<TextView>(R.id.recipe_ingredients_list)
    val instructions = view.findViewById<TextView>(R.id.recipe_instructions)
    val loadBar = view.findViewById<View>(R.id.progressBarDetail)
    val scrollView = view.findViewById<View>(R.id.content_scroll_view)
    var currentMealDetail: APIModel.MealDetail? = null


    randomViewModel.mealId.observe(viewLifecycleOwner) { id ->
        if (!id.isNullOrBlank()) {
            mealDetailViewModel.searchMealById(id)
        }
    }

    var randomLoading = false
    var detailLoading = false

    fun updateLoading() {
        setLoadingState(randomLoading || detailLoading, loadBar, scrollView)
    }

    randomViewModel.isLoading.observe(viewLifecycleOwner) {
        randomLoading = it
        updateLoading()
    }

    mealDetailViewModel.isLoading.observe(viewLifecycleOwner) {
        detailLoading = it
        updateLoading()
    }

    mealDetailViewModel.mealDetail.observe(viewLifecycleOwner) { mealDetail ->
        mealDetail?.let {
            currentMealDetail = it
            bookmarkViewModel.isBookmarked(it.id).observe(viewLifecycleOwner) { isBookmarked ->
                bookmark.isSelected = isBookmarked
                editButtonContents(bookmark)
                editButtonBG(bookmark)
            }
            banner.updateImage(it.imageUrl)
            title.updateText(it.name)
            country.updateText(" ${it.area}")
            ingredients.updateIngredients(it)
            instructions.updateInstructions(it)

            FirebaseDB.saveRecent(mealDetail) {
                Log.d("DetailFragment", "Saved to recently viewed")
            }
        }
    }

    bookmark.setOnClickListener {
        currentMealDetail?.let { it ->
            if (bookmark.isSelected) bookmarkViewModel.removeBookmark(it)
            else bookmarkViewModel.addBookmark(it)
            bookmark.isSelected = !bookmark.isSelected
            editButtonContents(bookmark)
            editButtonBG(bookmark)
        }
    }
}

/**
 * Toggles the visibility of the progress bar and content view based on loading state.
 * @param isLoading Boolean indicating if data is currently loading.
 * @param progressBar The progress bar view.
 * @param contentView The main content view to hide/show.
 */
private fun RandomFragment.setLoadingState(isLoading: Boolean, progressBar: View, contentView: View) {
    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    contentView.visibility = if (isLoading) View.GONE else View.VISIBLE
}

/**
 * Updates an ImageView with an image from a URL using Glide.
 * @param url The URL of the image.
 */
private fun ImageView.updateImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}

/**
 * Updates a TextView with the provided text.
 * @param text The text to display.
 */
private fun TextView.updateText(text: String?) {
    this.text = text
}

/**
 * Formats and displays the list of ingredients and measures.
 * @param mealDetail The meal detail object containing ingredients and measures.
 */
private fun TextView.updateIngredients(mealDetail: APIModel.MealDetail) {
    val ingredientList = (1..20)
        .mapNotNull { i ->
            val ingredient = mealDetail.getIngredient(i)
            val measure = mealDetail.getMeasure(i)
            if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) "$ingredient - $measure" else null
        }
        .joinToString("\n")
    this.text = ingredientList
}

/**
 * Formats and displays the cooking instructions.
 * @param mealDetail The meal detail object containing instructions.
 */
private fun TextView.updateInstructions(mealDetail: APIModel.MealDetail) {
    val instructionList = mealDetail.instructions
        ?.split("\n")
        ?.filter { it.isNotBlank() }
        ?.joinToString("\n\n")
    this.text = instructionList
}

private fun editButtonContents(btn: Button) {
    btn.text = if (btn.isSelected) "Bookmarked" else "Bookmark"
}

private fun editButtonBG(btn: Button){
    if (btn.isSelected){
        btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.active_yellow))
        btn.setTextColor(ContextCompat.getColor(btn.context, R.color.white))
        btn.setTypeface(null, android.graphics.Typeface.BOLD)
    }
    else {
        btn.setTextColor(ContextCompat.getColor(btn.context, R.color.inactive_black))
        btn.setBackgroundColor(Color.parseColor("#E7E7E7"))
        btn.setTypeface(null, android.graphics.Typeface.NORMAL)
    }

}