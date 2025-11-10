package com.example.l_tim_c_que.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.l_tim_c_que.R
import com.example.l_tim_c_que.api.APIModel

class MealAdapter(
    private val onClick: (APIModel.Meal) -> Unit
) : ListAdapter<APIModel.Meal, MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return MealViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)
    }
}

class MealViewHolder(
    itemView: View,
    private val onClick: (APIModel.Meal) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val mealImage: ImageView = itemView.findViewById(R.id.recipe_banner)
    private val mealName: TextView = itemView.findViewById(R.id.recipe_title)
    private val recipieBookmark: ImageButton = itemView.findViewById(R.id.recipie_bookmark)
    private var currentMeal: APIModel.Meal? = null


    init {
        itemView.setOnClickListener {
            currentMeal?.let {
                onClick(it)
            }
        }

        recipieBookmark.setOnClickListener {
            currentMeal?.let {
                onClick(it)
            }
        }
    }

    fun bind(meal: APIModel.Meal) {
        currentMeal = meal

        mealName.text = meal.name

        Glide.with(mealImage.context)
            .load(meal.imageUrl)
            .into(mealImage)
    }
}

class MealDiffCallback : DiffUtil.ItemCallback<APIModel.Meal>() {
    override fun areItemsTheSame(oldItem: APIModel.Meal, newItem: APIModel.Meal) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: APIModel.Meal, newItem: APIModel.Meal) =
        oldItem == newItem
}
