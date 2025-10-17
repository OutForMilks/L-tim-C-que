package com.example.l_tim_c_que

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val imageUrl: String,
    val ingredients: List<String>,
    var isBookmarked: Boolean = false
)