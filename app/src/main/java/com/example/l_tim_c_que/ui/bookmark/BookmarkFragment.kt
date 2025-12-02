package com.example.l_tim_c_que.ui.bookmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.api.APIClient
import com.example.l_tim_c_que.repository.MealRepository
import com.example.l_tim_c_que.ui.adapter.MealDetailAdapter
import com.example.l_tim_c_que.util.GridSpacingItemDecoration
import com.example.l_tim_c_que.viewmodel.BookmarkViewModel
import com.example.l_tim_c_que.viewmodel.BookmarkViewModelFactory
import com.example.l_tim_c_que.viewmodel.DetailViewModel
import kotlin.getValue

/**
 * Fragment responsible for displaying the user's bookmarked meals.
 */
class BookmarkFragment : Fragment() {
    internal val bookmarkViewModel: BookmarkViewModel by activityViewModels {
        BookmarkViewModelFactory(MealRepository(APIClient.api))
    }
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

    val emptyImage = emptyState.findViewById<ImageView>(R.id.empty_state_icon)
    val emptyStateText = emptyState.findViewById<TextView>(R.id.empty_state_text)

    emptyStateText.text ="You have not\nbookmarked\nany recipes."
    emptyImage.setImageResource(R.drawable.bookmark_icon)


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
    }
}