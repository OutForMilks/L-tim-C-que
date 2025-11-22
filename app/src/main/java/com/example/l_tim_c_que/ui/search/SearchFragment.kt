package com.example.l_tim_c_que.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.api.APIClient
import com.example.l_tim_c_que.repository.MealRepository
import com.example.l_tim_c_que.ui.adapter.MealAdapter
import com.example.l_tim_c_que.util.GridSpacingItemDecoration
import com.example.l_tim_c_que.viewmodel.DetailViewModel
import com.example.l_tim_c_que.viewmodel.MealViewModel
import com.example.l_tim_c_que.viewmodel.MealViewModelFactory
import com.example.l_tim_c_que.viewmodel.SearchViewModel
import android.widget.ImageView
import com.google.android.material.button.MaterialButton

/**
 * Fragment responsible for searching meals.
 * Handles search input, filtering, and displaying results in a grid.
 */
class SearchFragment : Fragment() {

    internal val searchViewModel: SearchViewModel by activityViewModels()

    internal val detailViewModel: DetailViewModel by activityViewModels()

    internal val mealViewModel: MealViewModel by activityViewModels {
        MealViewModelFactory(MealRepository(APIClient.api))
    }

    internal val adapter = MealAdapter { clickedMeal ->
        navigateToDetails(clickedMeal.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader(view)
        setupRecyclerView(view)
        setupObservers(view)
        setupSearchBar(view)
        setupFilterButtons(view)
    }

    private fun navigateToDetails(mealId: String) {
        detailViewModel.setDetailID(mealId)
        findNavController().navigate(R.id.action_search_to_detail)
    }
}

/**
 * Initializes the header section of the search screen.
 * Sets the title and visibility of the search bar.
 *
 * @param view The root view of the fragment.
 */
private fun SearchFragment.setupHeader(view: View) {
    val title = view.findViewById<TextView>(R.id.tvTitle)
    val search = view.findViewById<View>(R.id.search_bar)
    val emptyState = view.findViewById<View>(R.id.empty_state)
    
    title.visibility = View.VISIBLE
    search.visibility = View.VISIBLE
    emptyState.visibility = View.VISIBLE

    val emptyStateText = view.findViewById<TextView>(R.id.empty_state_text)
    emptyStateText.text ="You have not viewed\nany recipes."

    title.text = "Search Results"
}

/**
 * Sets up the RecyclerView for displaying meal results.
 * Configures the GridLayoutManager and adds item decoration for spacing.
 *
 * @param view The root view of the fragment.
 */
private fun SearchFragment.setupRecyclerView(view: View) {
    val recycleView = view.findViewById<RecyclerView>(R.id.recipie_widget_list)
    recycleView.adapter = adapter
    recycleView.layoutManager = GridLayoutManager(requireContext(), 2)
    val spacingInPixels = (22 * resources.displayMetrics.density).toInt()
    recycleView.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, false))
}

/**
 * Sets up observers for the ViewModels.
 * Observes loading state, meal list, and search queries to update the UI accordingly.
 *
 * @param view The root view of the fragment.
 */
private fun SearchFragment.setupObservers(view: View) {
    val loadBar = view.findViewById<View>(R.id.progressBar)
    val recycleView = view.findViewById<RecyclerView>(R.id.recipie_widget_list)
    val emptyState = view.findViewById<View>(R.id.empty_state)
    val etSearch = view.findViewById<EditText>(R.id.searchbar_text)

    val emptyImage = view.findViewById<ImageView>(R.id.empty_state_icon)
    val emptyText = view.findViewById<TextView>(R.id.empty_state_text)

    emptyImage.setImageResource(R.drawable.search_icon)

    mealViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        if (isLoading) {
            emptyState.visibility = View.GONE
            loadBar.visibility = View.VISIBLE

            recycleView.visibility = View.GONE
        } else {
            loadBar.visibility = View.GONE
            recycleView.visibility = View.VISIBLE
        }
    }

    mealViewModel.meals.observe(viewLifecycleOwner) { meals ->
        adapter.submitList(meals)
        emptyState.visibility = if (meals.isEmpty()) View.VISIBLE else View.GONE
        emptyText.text = "No results found."
    }

    searchViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
        if (etSearch.text.toString() != query) {
            etSearch.setText(query)
        }
        when (searchViewModel.filter.value) {
            "name" -> mealViewModel.searchMealByName(query)
            "origin" -> mealViewModel.searchMealByArea(query)
            "ingredient" -> mealViewModel.searchMealByIngredient(query)
            null -> mealViewModel.searchMealByName(query)
        }
    }
}

/**
 * Configures the search bar behavior.
 * Listens for the search action on the keyboard to trigger a search query.
 *
 * @param view The root view of the fragment.
 */
private fun SearchFragment.setupSearchBar(view: View) {
    val etSearch = view.findViewById<EditText>(R.id.searchbar_text)
    etSearch.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.setSearchQuery(query)
                hideKeyboard(etSearch)
                etSearch.clearFocus()
            }
            true
        } else {
            false
        }
    }
}

/**
 * Hides the software keyboard.
 *
 * @param view The view that currently has focus.
 */
private fun SearchFragment.hideKeyboard(view: View) {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Sets up the filter buttons and the toggle mechanism for the filter section.
 * Handles animations and state updates for filter selection.
 *
 * @param view The root view of the fragment.
 */
private fun SearchFragment.setupFilterButtons(view: View) {
    val filter = view.findViewById<View>(R.id.filter_buttons)
    val filterButton = view.findViewById<ImageButton>(R.id.filter_icon)

    filterButton.setOnClickListener {
        val parent = filter.parent as ViewGroup

        // Animate layout changes in the parent
        TransitionManager.beginDelayedTransition(parent, AutoTransition().apply {
            duration = 300 // drop-down duration in ms
        })

        // Toggle visibility
        filter.visibility = if (filter.isVisible) View.GONE else View.VISIBLE
    }

    val nameBtn = view.findViewById<MaterialButton>(R.id.nameBtn)
    val originBtn = view.findViewById<MaterialButton>(R.id.originBtn)
    val ingredientBtn = view.findViewById<MaterialButton>(R.id.ingredientBtn)

    val buttons = listOf(nameBtn, originBtn, ingredientBtn)

    when (searchViewModel.filter.value) {
        "name" -> nameBtn.setBackgroundResource(R.drawable.bg_filter_selected)
        "origin" -> originBtn.setBackgroundResource(R.drawable.bg_filter_selected)
        "ingredient" -> ingredientBtn.setBackgroundResource(R.drawable.bg_filter_selected)
    }

    buttons.forEach { button ->
        button.setOnClickListener {
            // Uncheck all buttons first
            buttons.forEach { it.setBackgroundResource(R.drawable.bg_filter) }
            // Check the clicked button
            button.setBackgroundResource(R.drawable.bg_filter_selected)
            // Update the ViewModel filter
            when (button.id) {
                R.id.nameBtn -> searchViewModel.setFilter("name")
                R.id.originBtn -> searchViewModel.setFilter("origin")
                R.id.ingredientBtn -> searchViewModel.setFilter("ingredient")
            }
        }
    }
}
