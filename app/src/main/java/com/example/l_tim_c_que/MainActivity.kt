package com.example.l_tim_c_que

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //recently viewed
        val recentlyViewedData = RecipeSamples.get()
        val recentlyViewedAdapter = RecentlyViewedAdapter(recentlyViewedData)
        val recentlyViewedRecyclerView = findViewById<RecyclerView>(R.id.recently_viewed_recipes)
        recentlyViewedRecyclerView.adapter = recentlyViewedAdapter
        recentlyViewedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val horizontalSpacing = (18 * resources.displayMetrics.density).toInt()
        recentlyViewedRecyclerView.addItemDecoration(HorizontalSpacingItemDecoration(horizontalSpacing))

        //random feature
        val randomFeatureData = RecipeSamples.get()
        val randomFeatureAdapter = RandomFeatureAdapter(randomFeatureData)
        val randomFeatureRecyclerView = findViewById<RecyclerView>(R.id.random_feature_recipes)
        randomFeatureRecyclerView.adapter = randomFeatureAdapter
        randomFeatureRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = (22 * resources.displayMetrics.density).toInt()
        randomFeatureRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, false))
    }
}