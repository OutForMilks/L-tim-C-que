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
import kotlin.getValue
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import com.bumptech.glide.Glide


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    private val mealViewModel: MealViewModel by activityViewModels {
        MealViewModelFactory(MealRepository(APIClient.api))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val banner = view.findViewById<ImageView>(R.id.recipe_banner)
        val title = view.findViewById<TextView>(R.id.recipe_title)
        val country = view.findViewById<TextView>(R.id.recipe_country)
        val bookmark = view.findViewById<Button>(R.id.bookmark_button)
        val ingredients = view.findViewById<TextView>(R.id.recipe_ingredients_list)
        val instructions = view.findViewById<TextView>(R.id.recipe_instructions)


        mealViewModel.mealDetail.observe(viewLifecycleOwner) { mealDetail ->
            if (mealDetail == null) {
                return@observe
            }
            else{
                Glide.with(banner.context).load(mealDetail.imageUrl).into(banner)
                title.text = mealDetail.name
                country.text = " ${mealDetail.area}"

                var ingredientList = ""

                for (i in 1..20) {
                    val ingredient = mealDetail.getIngredient(i)
                    val measure = mealDetail.getMeasure(i)
                    if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                        ingredientList += "$ingredient - $measure\n"
                    }
                }
                ingredients.text = ingredientList

                var instructionList = ""
                val instructionsList = mealDetail.instructions?.split("\n")
                if (instructionsList != null) {
                    for (instruction in instructionsList) {
                        if (instruction.isNotBlank()) {
                            instructionList += "$instruction\n\n"
                        }
                    }
                }
                instructions.text = instructionList
            }


        }
    }

}
