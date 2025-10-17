package com.example.l_tim_c_que

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecentlyViewedAdapter(private val recipeList: ArrayList<Recipe>) :
    RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedViewHolder>() {

    inner class RecentlyViewedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeBanner: ImageView = itemView.findViewById(R.id.recipe_banner)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipe_title)
        val recipeCategories: TextView = itemView.findViewById(R.id.recipe_categories)
        val favIcon: ImageButton = itemView.findViewById(R.id.fav_icon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyViewedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return RecentlyViewedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecentlyViewedViewHolder, position: Int) {
        val currentRecipe = recipeList[position]

        holder.recipeTitle.text = currentRecipe.name

        holder.recipeCategories.text = "${currentRecipe.category} • ${currentRecipe.area}"

        Glide.with(holder.itemView.context)
            .load(currentRecipe.imageUrl)
            .placeholder(R.drawable.chicken_mushroom_bg)
            .centerCrop()
            .into(holder.recipeBanner)

        if (currentRecipe.isBookmarked) {
            holder.favIcon.setImageResource(R.drawable.favorite_icon_filled)
        } else {
            holder.favIcon.setImageResource(R.drawable.favorite_icon)
        }

        holder.favIcon.setOnClickListener {
            currentRecipe.isBookmarked = !currentRecipe.isBookmarked
        }
    }
}