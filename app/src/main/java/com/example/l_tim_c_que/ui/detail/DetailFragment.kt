package com.example.l_tim_c_que.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.api.APIClient
import com.example.l_tim_c_que.repository.MealRepository
import com.example.l_tim_c_que.viewmodel.MealViewModel
import com.example.l_tim_c_que.viewmodel.MealViewModelFactory
import com.example.l_tim_c_que.viewmodel.DetailViewModel
import com.example.l_tim_c_que.viewmodel.MealDetailViewModel
import com.example.l_tim_c_que.viewmodel.MealDetailViewModelFactory
import kotlin.getValue
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.l_tim_c_que.api.APIModel
import com.example.l_tim_c_que.firebase.FirebaseDB


/**
 * A simple [Fragment] subclass for displaying meal details.
 * Uses ViewModels to fetch and observe meal data.
 */
class DetailFragment : Fragment() {

    internal val mealDetailViewModel: MealDetailViewModel by activityViewModels {
        MealDetailViewModelFactory(MealRepository(APIClient.api))
    }

    internal val detailViewModel: DetailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
private fun DetailFragment.setupViews(view: View) {
    // No specific initial setup needed beyond findViewById which is done in update methods or observers
    // But we can keep it for structure or future initializations
}

/**
 * Sets up observers for ViewModels to update the UI when data changes.
 * @param view The root view of the fragment.
 */
private fun DetailFragment.setupObservers(view: View) {
    val banner = view.findViewById<ImageView>(R.id.recipe_banner)
    val title = view.findViewById<TextView>(R.id.recipe_title)
    val country = view.findViewById<TextView>(R.id.recipe_country)
    val bookmark = view.findViewById<Button>(R.id.bookmark_button) // Unused currently
    val ingredients = view.findViewById<TextView>(R.id.recipe_ingredients_list)
    val instructions = view.findViewById<TextView>(R.id.recipe_instructions)
    val loadBar = view.findViewById<View>(R.id.progressBarDetail)
    val scrollView = view.findViewById<View>(R.id.content_scroll_view)
    var currentMealDetail: APIModel.MealDetail? = null

    detailViewModel.detailID.observe(viewLifecycleOwner) { id ->
        mealDetailViewModel.searchMealById(id)
    }

    mealDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        setLoadingState(isLoading, loadBar, scrollView)
    }

    mealDetailViewModel.mealDetail.observe(viewLifecycleOwner) { mealDetail ->
        mealDetail?.let {
            currentMealDetail = it
            banner.updateImage(it.imageUrl)
            title.updateText(it.name)
            country.updateText(" ${it.area}")
            ingredients.updateIngredients(it)
            instructions.updateInstructions(it)
            bookmark.bookmarkStatus(it.id)
        }
    }

    bookmark.setOnClickListener {
        currentMealDetail?.let { it -> bookmark.updateBookmark(it)}
    }
}

/**
 * Toggles the visibility of the progress bar and content view based on loading state.
 * @param isLoading Boolean indicating if data is currently loading.
 * @param progressBar The progress bar view.
 * @param contentView The main content view to hide/show.
 */
private fun DetailFragment.setLoadingState(isLoading: Boolean, progressBar: View, contentView: View) {
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
 * Sets Button with the provided meal's bookmark status.
 * @param meal The id of the meal.
 */
private fun Button.bookmarkStatus(meal: String) {
    FirebaseDB.isInBookmarks(meal) { bool ->
        this.isSelected = bool
        editButtonContents(this)
    }
}

private fun editButtonContents(btn: Button) {
    btn.text = if (btn.isSelected) "Bookmarked" else "Bookmark"
}

/**
 * Updates bookmarked status of the provided meal.
 * @param meal The meal of type MealDetail.
 */
private fun Button.updateBookmark(meal: APIModel.MealDetail) {
    FirebaseDB.isInBookmarks(meal.id) { bool ->
        if (bool) FirebaseDB.removeBookmark(meal) { it ->
            this.isSelected = !it
            editButtonContents(this)
        }
        else FirebaseDB.saveBookmark(meal) { it ->
            this.isSelected = it
            editButtonContents(this)
        }
    }
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

