package com.example.l_tim_c_que.api

import com.google.gson.annotations.SerializedName

object APIModel {

    data class MealListResponse(
        @SerializedName("meals")
        val meals: List<Meal>?
    )

    data class Meal(
        @SerializedName("idMeal")
        val id: String,
        @SerializedName("strMeal") // Meal Name
        val name: String?,
        @SerializedName("strMealThumb") // Meal image url
        val imageUrl: String?
    )

    data class MealDetailResponse(
        @SerializedName("meals")
        val meals: List<MealDetail>?
    )

    data class MealDetail(
        @SerializedName("idMeal")
        val id: String,
        @SerializedName("strMeal") // Meal Name
        val name: String?,
        @SerializedName("strCategory") // Meal Category
        val category: String?,
        @SerializedName("strArea") // Meal Place of Origin
        val area: String?,
        @SerializedName("strInstructions") // Meal Instructions
        val instructions: String?,
        @SerializedName("strMealThumb") // Meal image url
        val imageUrl: String?,
        @SerializedName("strIngredient1") // Meal Ingredient 1
        val ingredient1: String?,
        @SerializedName("strIngredient2") // Meal Ingredient 2
        val ingredient2: String?,
        @SerializedName("strIngredient3") // Meal Ingredient 3
        val ingredient3: String?,
        @SerializedName("strIngredient4") // Meal Ingredient 4
        val ingredient4: String?,
        @SerializedName("strIngredient5") // Meal Ingredient 5
        val ingredient5: String?,
        @SerializedName("strIngredient6") // Meal Ingredient 6
        val ingredient6: String?,
        @SerializedName("strIngredient7") // Meal Ingredient 7
        val ingredient7: String?,
        @SerializedName("strIngredient8") // Meal Ingredient 8
        val ingredient8: String?,
        @SerializedName("strIngredient9") // Meal Ingredient 9
        val ingredient9: String?,
        @SerializedName("strIngredient10") // Meal Ingredient 10
        val ingredient10: String?,
        @SerializedName("strIngredient11") // Meal Ingredient 11
        val ingredient11: String?,
        @SerializedName("strIngredient12") // Meal Ingredient 12
        val ingredient12: String?,
        @SerializedName("strIngredient13") // Meal Ingredient 13
        val ingredient13: String?,
        @SerializedName("strIngredient14") // Meal Ingredient 14
        val ingredient14: String?,
        @SerializedName("strIngredient15") // Meal Ingredient 15
        val ingredient15: String?,
        @SerializedName("strIngredient16") // Meal Ingredient 16
        val ingredient16: String?,
        @SerializedName("strIngredient17") // Meal Ingredient 17
        val ingredient17: String?,
        @SerializedName("strIngredient18") // Meal Ingredient 18
        val ingredient18: String?,
        @SerializedName("strIngredient19") // Meal Ingredient 19
        val ingredient19: String?,
        @SerializedName("strIngredient20") // Meal Ingredient 20
        val ingredient20: String?,
        @SerializedName("strMeasure1") // Meal Measure 1
        val measure1: String?,
        @SerializedName("strMeasure2") // Meal Measure 2
        val measure2: String?,
        @SerializedName("strMeasure3") // Meal Measure 3
        val measure3: String?,
        @SerializedName("strMeasure4") // Meal Measure 4
        val measure4: String?,
        @SerializedName("strMeasure5") // Meal Measure 5
        val measure5: String?,
        @SerializedName("strMeasure6") // Meal Measure 6
        val measure6: String?,
        @SerializedName("strMeasure7") // Meal Measure 7
        val measure7: String?,
        @SerializedName("strMeasure8") // Meal Measure 8
        val measure8: String?,
        @SerializedName("strMeasure9") // Meal Measure 9
        val measure9: String?,
        @SerializedName("strMeasure10") // Meal Measure 10
        val measure10: String?,
        @SerializedName("strMeasure11") // Meal Measure 11
        val measure11: String?,
        @SerializedName("strMeasure12") // Meal Measure 12
        val measure12: String?,
        @SerializedName("strMeasure13") // Meal Measure 13
        val measure13: String?,
        @SerializedName("strMeasure14") // Meal Measure 14
        val measure14: String?,
        @SerializedName("strMeasure15") // Meal Measure 15
        val measure15: String?,
        @SerializedName("strMeasure16") // Meal Measure 16
        val measure16: String?,
        @SerializedName("strMeasure17") // Meal Measure 17
        val measure17: String?,
        @SerializedName("strMeasure18") // Meal Measure 18
        val measure18: String?,
        @SerializedName("strMeasure19") // Meal Measure 19
        val measure19: String?,
        @SerializedName("strMeasure20") // Meal Measure 20
        val measure20: String?
    ) {
        fun getMeasure(i: Int): String? = when(i) {
            1 -> measure1
            2 -> measure2
            3 -> measure3
            4 -> measure4
            5 -> measure5
            6 -> measure6
            7 -> measure7
            8 -> measure8
            9 -> measure9
            10 -> measure10
            11 -> measure11
            12 -> measure12
            13 -> measure13
            14 -> measure14
            15 -> measure15
            16 -> measure16
            17 -> measure17
            18 -> measure18
            19 -> measure19
            20 -> measure20
            else -> null
        }

        fun getIngredient(i: Int): String? = when(i) {
            1 -> ingredient1
            2 -> ingredient2
            3 -> ingredient3
            4 -> ingredient4
            5 -> ingredient5
            6 -> ingredient6
            7 -> ingredient7
            8 -> ingredient8
            9 -> ingredient9
            10 -> ingredient10
            11 -> ingredient11
            12 -> ingredient12
            13 -> ingredient13
            14 -> ingredient14
            15 -> ingredient15
            16 -> ingredient16
            17 -> ingredient17
            18 -> ingredient18
            19 -> ingredient19
            20 -> ingredient20
            else -> null
        }

    }


}