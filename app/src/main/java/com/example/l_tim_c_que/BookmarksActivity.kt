package com.example.l_tim_c_que

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookmarksActivity : AppCompatActivity() {
    private lateinit var homeItem: LinearLayout
    private lateinit var searchItem: LinearLayout
    private lateinit var recentsItem: LinearLayout
    private lateinit var bookmarksItem: LinearLayout

    private lateinit var homeIcon: ImageView
    private lateinit var searchIcon: ImageView
    private lateinit var recentsIcon: ImageView
    private lateinit var bookmarksIcon: ImageView

    private lateinit var homeText: TextView
    private lateinit var searchText: TextView
    private lateinit var recentsText: TextView
    private lateinit var bookmarksText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bookmarks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bookmarks)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeItem = findViewById(R.id.home_item)
        searchItem = findViewById(R.id.search_item)
        recentsItem = findViewById(R.id.recents_item)
        bookmarksItem = findViewById(R.id.bookmark_item)

        homeIcon = findViewById(R.id.home_icon)
        searchIcon = findViewById(R.id.search_icon)
        recentsIcon = findViewById(R.id.recents_icon)
        bookmarksIcon = findViewById(R.id.bookmark_icon)

        homeText = findViewById(R.id.home_text)
        searchText = findViewById(R.id.search_text)
        recentsText = findViewById(R.id.recents_text)
        bookmarksText = findViewById(R.id.bookmark_text)

        updateNavColors(R.id.bookmark_item)

        val allRecipes = RecipeSamples.get()
        val bookmarkData = allRecipes.filter { it.isBookmarked }.toCollection(ArrayList())
        val bookmarksRecyclerView = findViewById<RecyclerView>(R.id.bookmarks_recycler_view)
        val bookmarksAdapter = BookmarksAdapter(bookmarkData)
        bookmarksRecyclerView.adapter = bookmarksAdapter
        bookmarksRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = (22 * resources.displayMetrics.density).toInt()
        bookmarksRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, false))
        bookmarksAdapter.setOnItemClickListener { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)

            intent.putExtra("EXTRA_RECIPE", recipe)

            startActivity(intent)
        }

        bookmarksAdapter.setOnBookmarkClickListener { recipe, position ->
            // database logic here
            recipe.isBookmarked = !recipe.isBookmarked

            bookmarksAdapter.notifyItemChanged(position)
        }

        searchItem.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        recentsItem.setOnClickListener {
            val intent = Intent(this, RecentsActivity::class.java)
            startActivity(intent)
        }

        homeItem.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun updateNavColors(activeItemId: Int) {
        val activeColor = ContextCompat.getColor(this, R.color.active_yellow) // #E5AF00
        val inactiveColor = ContextCompat.getColor(this, R.color.inactive_black) // #000000

        homeIcon.imageTintList = ColorStateList.valueOf(inactiveColor)
        homeText.setTextColor(inactiveColor)

        searchIcon.imageTintList = ColorStateList.valueOf(inactiveColor)
        searchText.setTextColor(inactiveColor)

        recentsIcon.imageTintList = ColorStateList.valueOf(inactiveColor)
        recentsText.setTextColor(inactiveColor)

        bookmarksIcon.imageTintList = ColorStateList.valueOf(inactiveColor)
        bookmarksText.setTextColor(inactiveColor)

        when (activeItemId) {
            R.id.home_item -> {
                homeIcon.imageTintList = ColorStateList.valueOf(activeColor)
                homeText.setTextColor(activeColor)
            }
            R.id.search_item -> {
                searchIcon.imageTintList = ColorStateList.valueOf(activeColor)
                searchText.setTextColor(activeColor)
            }
            R.id.recents_item -> {
                recentsIcon.imageTintList = ColorStateList.valueOf(activeColor)
                recentsText.setTextColor(activeColor)
            }
            R.id.bookmark_item -> {
                bookmarksIcon.imageTintList = ColorStateList.valueOf(activeColor)
                bookmarksText.setTextColor(activeColor)
            }
        }
    }
}