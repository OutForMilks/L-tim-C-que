package com.example.l_tim_c_que.ui.recent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.l_tim_c_que.R

/**
 * Fragment responsible for displaying the user's recently viewed meals.
 *
 * This fragment is currently a placeholder and inflates the `fragment_recent` layout.
 * Future implementation should include fetching and displaying the recent history from a local database or repository.
 */
class RecentFragment : Fragment() {

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }
}
