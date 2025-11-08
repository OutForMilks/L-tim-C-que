package com.example.l_tim_c_que.ui.home

import android.content.Context
import android.os.Bundle

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels

import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private val shareViewModel: SharedViewModel by activityViewModels()

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

        title.visibility = View.VISIBLE
        search.visibility = View.VISIBLE
        header.visibility = View.VISIBLE

        val etSearch = view.findViewById<EditText>(R.id.searchbar_text)

        etSearch.setOnEditorActionListener { _, actionId, event ->
            val isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH
            val isEnterKey = event != null &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER &&
                    event.action == KeyEvent.ACTION_DOWN

            if (isSearchAction || isEnterKey) {
                val query = etSearch.text.toString().trim()
                if (query.isNotEmpty()) {

                    shareViewModel.setSearchQuery(query)

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
    }
}
