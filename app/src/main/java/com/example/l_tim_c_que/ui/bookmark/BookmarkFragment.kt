package com.example.l_tim_c_que.ui.bookmark

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.api.APIClient
import com.example.l_tim_c_que.repository.MealRepository
import com.example.l_tim_c_que.ui.adapter.MealDetailAdapter
import com.example.l_tim_c_que.util.GridSpacingItemDecoration
import com.example.l_tim_c_que.viewmodel.BookmarkViewModel
import com.example.l_tim_c_que.viewmodel.BookmarkViewModelFactory
import com.example.l_tim_c_que.viewmodel.DetailViewModel
import com.example.l_tim_c_que.viewmodel.MealViewModel
import com.example.l_tim_c_que.viewmodel.MealViewModelFactory
import com.example.l_tim_c_que.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import kotlin.getValue
import com.example.l_tim_c_que.ui.bookmark.BookmarkFragmentDirections

/**
 * Fragment responsible for displaying the user's bookmarked meals.
 */
class BookmarkFragment : Fragment() {
    internal val bookmarkViewModel: BookmarkViewModel by activityViewModels {
        BookmarkViewModelFactory(MealRepository(APIClient.api))
    }
    internal val mealViewModel: MealViewModel by activityViewModels()
    internal val searchViewModel: SearchViewModel by activityViewModels()
    internal val detailViewModel: DetailViewModel by activityViewModels()

    internal val adapter = MealDetailAdapter { clickedMeal ->
        navigateToDetails(clickedMeal.id)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader(view)
        setupRecyclerView(view)
        setupObservers(view)
        setupSearchBar(view)
        setupFilterButtons(view)

        bookmarkViewModel.loadBookmarks()
        Log.d("BookmarkFragment", bookmarkViewModel.mealDetailsList.toString())
    }

    private fun navigateToDetails(mealId: String) {
        detailViewModel.setDetailID(mealId)
        findNavController().navigate(R.id.action_bookmark_to_detail)
    }
}

/**
 * Initializes the header section of the search screen.
 * Sets the title and visibility of the search bar.
 *
 * @param view The root view of the fragment.
 */
private fun BookmarkFragment.setupHeader(view: View) {
    val title = view.findViewById<TextView>(R.id.tvTitle)
    val search = view.findViewById<View>(R.id.search_bar)
    val emptyState = view.findViewById<View>(R.id.empty_state)

    title.visibility = View.VISIBLE
    search.visibility = View.VISIBLE
    emptyState.visibility = View.VISIBLE

    val emptyStateText = view.findViewById<TextView>(R.id.empty_state_text)
    emptyStateText.text ="You have not bookmarked\nany recipes."

    title.text = "Bookmarked Recipes"
}

/**
 * Sets up the RecyclerView for displaying meal results.
 * Configures the GridLayoutManager and adds item decoration for spacing.
 *
 * @param view The root view of the fragment.
 */
private fun BookmarkFragment.setupRecyclerView(view: View) {
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
private fun BookmarkFragment.setupObservers(view: View) {
    val loadBar = view.findViewById<View>(R.id.progressBar)
    val recycleView = view.findViewById<RecyclerView>(R.id.recipie_widget_list)
    val emptyState = view.findViewById<View>(R.id.empty_state)
    val etSearch = view.findViewById<EditText>(R.id.searchbar_text)

    val emptyImage = view.findViewById<ImageView>(R.id.empty_state_icon)
    val emptyText = view.findViewById<TextView>(R.id.empty_state_text)

    emptyImage.setImageResource(R.drawable.search_icon)

    bookmarkViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        if (isLoading) {
            emptyState.visibility = View.GONE
            loadBar.visibility = View.VISIBLE

            recycleView.visibility = View.GONE
        } else {
            loadBar.visibility = View.GONE
            recycleView.visibility = View.VISIBLE
        }
    }

    bookmarkViewModel.mealDetailsList.observe(viewLifecycleOwner) { meals ->
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
 * Listens for the search action on the keyboard, updates the search query,
 * navigates to the search fragment, and hides the keyboard.
 *
 * @param view The root view of the fragment.
 */
private fun BookmarkFragment.setupSearchBar(view: View) {
    val etSearch = view.findViewById<EditText>(R.id.searchbar_text)

    etSearch.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query, etSearch)
            }
            true
        } else {
            false
        }
    }
}

/**
 * Performs the search operation.
 * Updates the ViewModel, sets the default filter if needed,
 * navigates to the SearchFragment via the BottomNavigationView, and hides the keyboard.
 *
 * @param query The search query string.
 * @param etSearch The EditText view for search input.
 */
private fun BookmarkFragment.performSearch(query: String, etSearch: EditText) {
    searchViewModel.setSearchQuery(query)
    if (searchViewModel.filter.value == null) {
        searchViewModel.setFilter("name")
    }

    val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

    bottomNav.post {
        bottomNav.selectedItemId = R.id.searchFragment
    }

    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
}

/**
 * Sets up the filter buttons and the toggle mechanism for the filter section.
 * Handles animations and state updates for filter selection.
 *
 * @param view The root view of the fragment.
 */
private fun BookmarkFragment.setupFilterButtons(view: View) {
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
