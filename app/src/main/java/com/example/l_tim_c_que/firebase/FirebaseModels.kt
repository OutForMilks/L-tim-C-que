package com.example.l_tim_c_que.firebase

import com.example.l_tim_c_que.api.APIModel
import com.google.firebase.Timestamp

object FirebaseModels {

    data class Bookmark(
        val user_id: String,
        val meal_id: String,
        val recipe: APIModel.MealDetail
    )

    data class Recent(
        val user_id: String,
        val meal_id: String,
        val recipe: APIModel.MealDetail,
        val timestamp: Timestamp
    )
}
