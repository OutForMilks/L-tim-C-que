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

/**
 * RecyclerView Adapter for displaying a list of meals.
 * Supports efficient updates via [ListAdapter] and [DiffUtil].
 *
 * @property onClick Callback function to be invoked when a meal item is clicked.
 */
class MealDetailAdapter(
    private val onClick: (APIModel.MealDetail) -> Unit
) : ListAdapter<APIModel.MealDetail?, MealDetailAdapter.MealViewHolder>(MealDetailDiffCallback()) {

    /**
     * ViewHolder class to hold references to the views for each meal item.
     */
    inner class MealViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val mealImage: ImageView = itemView.findViewById(R.id.recipe_banner)
        private val mealName: TextView = itemView.findViewById(R.id.recipe_title)

        /**
         * Binds the meal data to the views.
         *
         * @param meal The meal object to display.
         */
        fun bind(meal : APIModel.MealDetail)
        {
            val currentMeal = meal.toMeal()
            mealName.text = currentMeal.name
            Glide.with(mealImage.context).load(currentMeal.imageUrl).into(mealImage)

            itemView.setOnClickListener {
                onClick(meal)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal!!)
    }


}

/**
 * DiffCallback for calculating the difference between two non-null items in a list.
 * Used by [ListAdapter] to determine which items have changed.
 */
class MealDetailDiffCallback : DiffUtil.ItemCallback<APIModel.MealDetail?>() {
    override fun areItemsTheSame(oldItem: APIModel.MealDetail, newItem: APIModel.MealDetail) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: APIModel.MealDetail, newItem: APIModel.MealDetail) =
        oldItem == newItem
}
