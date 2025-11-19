package com.example.l_tim_c_que.ui.home

import android.content.Context
import android.os.Bundle

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton

import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<TextView>(R.id.tvTitle)
        val search = view.findViewById<View>(R.id.search_bar)
        val header = view.findViewById<View>(R.id.header)
        val filter = view.findViewById<View>(R.id.filter_buttons)

        title.visibility = View.VISIBLE
        search.visibility = View.VISIBLE
        header.visibility = View.VISIBLE

        val etSearch = view.findViewById<EditText>(R.id.searchbar_text)

        etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = etSearch.text.toString().trim()
                if (query.isNotEmpty()) {

                    searchViewModel.setSearchQuery(query)
                    searchViewModel.setFilter("name")

                    val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

                    bottomNav.post {
                        bottomNav.selectedItemId = R.id.searchFragment
                    }

                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
                }
                true
            } else {
                false
            }
        }

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

}
