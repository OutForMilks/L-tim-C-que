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

    private var onItemClickListener: ((Recipe) -> Unit)? = null
    private var onBookmarkClickListener: ((Recipe, Int) -> Unit)? = null

    inner class RecentlyViewedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeBanner: ImageView = itemView.findViewById(R.id.recipe_banner)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipe_title)
        val recipeCategories: TextView = itemView.findViewById(R.id.recipe_categories)
        val favIcon: ImageButton = itemView.findViewById(R.id.fav_icon)

        fun bind(recipe: Recipe) {
            recipeTitle.text = recipe.name
            recipeCategories.text = "${recipe.category} • ${recipe.area}"

            Glide.with(itemView.context)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.chicken_mushroom_bg)
                .centerCrop()
                .into(recipeBanner)

            if (recipe.isBookmarked) {
                favIcon.setImageResource(R.drawable.favorite_icon_filled)
            } else {
                favIcon.setImageResource(R.drawable.favorite_icon)
            }

            itemView.setOnClickListener {
                onItemClickListener?.invoke(recipe)
            }

            favIcon.setOnClickListener {
                onBookmarkClickListener?.invoke(recipe, adapterPosition)
            }
        }
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
        holder.bind(recipeList[position])
    }

    fun setOnItemClickListener(listener: (Recipe) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (Recipe, Int) -> Unit) {
        onBookmarkClickListener = listener
    }
}