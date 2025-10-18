package com.example.l_tim_c_que

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge // 1. Import this
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat // 2. Import this
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import java.io.Serializable

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var homeItem: LinearLayout
    private lateinit var searchItem: LinearLayout
    private lateinit var recentsItem: LinearLayout
    private lateinit var bookmarkItem: LinearLayout

    private lateinit var homeIcon: ImageView
    private lateinit var searchIcon: ImageView
    private lateinit var recentsIcon: ImageView
    private lateinit var bookmarkIcon: ImageView

    private lateinit var homeText: TextView
    private lateinit var searchText: TextView
    private lateinit var recentsText: TextView
    private lateinit var bookmarkText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recipe_details)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val banner: ImageView = findViewById(R.id.recipe_banner)
        val title: TextView = findViewById(R.id.recipe_title)
        val categories: TextView = findViewById(R.id.recipe_categories)
        val ingredientsList: TextView = findViewById(R.id.recipe_ingredients_list)
        val instructions: TextView = findViewById(R.id.recipe_instructions)

        val bookmarkButton: TextView = findViewById(R.id.bookmark_button)

        val navBar = findViewById<ConstraintLayout>(R.id.nav_bar_container)
        homeItem = navBar.findViewById(R.id.home_item)
        searchItem = navBar.findViewById(R.id.search_item)
        recentsItem = navBar.findViewById(R.id.recents_item)
        bookmarkItem = navBar.findViewById(R.id.bookmark_item)

        homeIcon = navBar.findViewById(R.id.home_icon)
        searchIcon = navBar.findViewById(R.id.search_icon)
        recentsIcon = navBar.findViewById(R.id.recents_icon)
        bookmarkIcon = navBar.findViewById(R.id.bookmark_icon)

        homeText = navBar.findViewById(R.id.home_text)
        searchText = navBar.findViewById(R.id.search_text)
        recentsText = navBar.findViewById(R.id.recents_text)
        bookmarkText = navBar.findViewById(R.id.bookmark_text)

        homeItem.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        searchItem.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        recentsItem.setOnClickListener {
            val intent = Intent(this, RecentsActivity::class.java)
            startActivity(intent)
        }

        bookmarkItem.setOnClickListener {
            val intent = Intent(this, BookmarksActivity::class.java)
            startActivity(intent)
        }

        val recipe = getSerializable(intent, "EXTRA_RECIPE", Recipe::class.java)

        if (recipe != null) {
            Glide.with(this).load(recipe.imageUrl).centerCrop().into(banner)
            title.text = recipe.name
            categories.text = "${recipe.category} • ${recipe.area}"

            val ingredientsBuilder = StringBuilder()
            for (ingredient in recipe.ingredients) {
                ingredientsBuilder.append("\u2022 ")
                ingredientsBuilder.append(ingredient)
                ingredientsBuilder.append("\n")
            }
            ingredientsList.text = ingredientsBuilder.toString().trimEnd()

            instructions.text = recipe.instructions

            fun updateBookmarkState() {
                if (recipe.isBookmarked) {
                    bookmarkButton.text = "Bookmarked"
                    bookmarkButton.setTextColor(ContextCompat.getColor(this, R.color.active_yellow))
                } else {
                    bookmarkButton.text = "Bookmark"
                    bookmarkButton.setTextColor(ContextCompat.getColor(this, R.color.inactive_black))
                }
            }

            updateBookmarkState()

            bookmarkButton.setOnClickListener {
                recipe.isBookmarked = !recipe.isBookmarked // Toggle the state
                updateBookmarkState() // Update the button's appearance

                // sqlite logic should be here
            }
        }
    }

    private fun <T : Serializable?> getSerializable(intent: Intent, key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(key, clazz)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(key) as? T
        }
    }
}