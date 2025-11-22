package com.example.l_tim_c_que.api

import com.google.gson.annotations.SerializedName

/**
 * Object containing Data Classes representing the JSON response from the MealDB API.
 */
object APIModel {

    /**
     * Response wrapper for a list of meals.
     *
     * @property meals A nullable list of [Meal] objects.
     */
    data class MealListResponse(
        @SerializedName("meals")
        val meals: List<Meal>?
    )

    /**
     * Represents a simplified meal object (e.g., from search results).
     *
     * @property id The unique ID of the meal.
     * @property name The name of the meal.
     * @property imageUrl The URL of the meal's thumbnail image.
     * @property area The area or origin of the meal (e.g., "Italian").
     */
    data class Meal(
        @SerializedName("idMeal")
        val id: String,
        @SerializedName("strMeal") // Meal Name
        val name: String?,
        @SerializedName("strMealThumb") // Meal image url
        val imageUrl: String?,
        @SerializedName("strArea") // Meal Place of Origin
        val area: String?
    )

    /**
     * Response wrapper for meal details.
     *
     * @property meals A nullable list of [MealDetail] objects (usually contains one item).
     */
    data class MealDetailResponse(
        @SerializedName("meals")
        val meals: List<MealDetail>?
    )

    /**
     * Represents detailed information about a specific meal.
     * Includes ingredients, measurements, instructions, and more.
     *
     * @property id The unique ID of the meal.
     * @property name The name of the meal.
     * @property category The category of the meal (e.g., "Seafood").
     * @property area The area or origin of the meal.
     * @property instructions The cooking instructions.
     * @property imageUrl The URL of the meal's thumbnail image.
     * @property ingredient1 Ingredient 1.
     * @property measure1 Measure for Ingredient 1.
     * ... (up to 20 ingredients and measures)
     */
    data class MealDetail(
        @SerializedName("idMeal")
        val id: String = "",
        @SerializedName("strMeal") // Meal Name
        val name: String? = null,
        @SerializedName("strCategory") // Meal Category
        val category: String? = null,
        @SerializedName("strArea") // Meal Place of Origin
        val area: String? = null,
        @SerializedName("strInstructions") // Meal Instructions
        val instructions: String? = null,
        @SerializedName("strMealThumb") // Meal image url
        val imageUrl: String? = null,
        @SerializedName("strIngredient1") // Meal Ingredient 1
        val ingredient1: String? = null,
        @SerializedName("strIngredient2") // Meal Ingredient 2
        val ingredient2: String? = null,
        @SerializedName("strIngredient3") // Meal Ingredient 3
        val ingredient3: String? = null,
        @SerializedName("strIngredient4") // Meal Ingredient 4
        val ingredient4: String? = null,
        @SerializedName("strIngredient5") // Meal Ingredient 5
        val ingredient5: String? = null,
        @SerializedName("strIngredient6") // Meal Ingredient 6
        val ingredient6: String? = null,
        @SerializedName("strIngredient7") // Meal Ingredient 7
        val ingredient7: String? = null,
        @SerializedName("strIngredient8") // Meal Ingredient 8
        val ingredient8: String? = null,
        @SerializedName("strIngredient9") // Meal Ingredient 9
        val ingredient9: String? = null,
        @SerializedName("strIngredient10") // Meal Ingredient 10
        val ingredient10: String? = null,
        @SerializedName("strIngredient11") // Meal Ingredient 11
        val ingredient11: String? = null,
        @SerializedName("strIngredient12") // Meal Ingredient 12
        val ingredient12: String? = null,
        @SerializedName("strIngredient13") // Meal Ingredient 13
        val ingredient13: String? = null,
        @SerializedName("strIngredient14") // Meal Ingredient 14
        val ingredient14: String? = null,
        @SerializedName("strIngredient15") // Meal Ingredient 15
        val ingredient15: String? = null,
        @SerializedName("strIngredient16") // Meal Ingredient 16
        val ingredient16: String? = null,
        @SerializedName("strIngredient17") // Meal Ingredient 17
        val ingredient17: String? = null,
        @SerializedName("strIngredient18") // Meal Ingredient 18
        val ingredient18: String? = null,
        @SerializedName("strIngredient19") // Meal Ingredient 19
        val ingredient19: String? = null,
        @SerializedName("strIngredient20") // Meal Ingredient 20
        val ingredient20: String? = null,
        @SerializedName("strMeasure1") // Meal Measure 1
        val measure1: String? = null,
        @SerializedName("strMeasure2") // Meal Measure 2
        val measure2: String? = null,
        @SerializedName("strMeasure3") // Meal Measure 3
        val measure3: String? = null,
        @SerializedName("strMeasure4") // Meal Measure 4
        val measure4: String? = null,
        @SerializedName("strMeasure5") // Meal Measure 5
        val measure5: String? = null,
        @SerializedName("strMeasure6") // Meal Measure 6
        val measure6: String? = null,
        @SerializedName("strMeasure7") // Meal Measure 7
        val measure7: String? = null,
        @SerializedName("strMeasure8") // Meal Measure 8
        val measure8: String? = null,
        @SerializedName("strMeasure9") // Meal Measure 9
        val measure9: String? = null,
        @SerializedName("strMeasure10") // Meal Measure 10
        val measure10: String? = null,
        @SerializedName("strMeasure11") // Meal Measure 11
        val measure11: String? = null,
        @SerializedName("strMeasure12") // Meal Measure 12
        val measure12: String? = null,
        @SerializedName("strMeasure13") // Meal Measure 13
        val measure13: String? = null,
        @SerializedName("strMeasure14") // Meal Measure 14
        val measure14: String? = null,
        @SerializedName("strMeasure15") // Meal Measure 15
        val measure15: String? = null,
        @SerializedName("strMeasure16") // Meal Measure 16
        val measure16: String? = null,
        @SerializedName("strMeasure17") // Meal Measure 17
        val measure17: String? = null,
        @SerializedName("strMeasure18") // Meal Measure 18
        val measure18: String? = null,
        @SerializedName("strMeasure19") // Meal Measure 19
        val measure19: String? = null,
        @SerializedName("strMeasure20") // Meal Measure 20
        val measure20: String? = null
    ) {
        /**
         * Retrieves a specific measure by index (1-20).
         *
         * @param i The index of the measure to retrieve.
         * @return The measure string, or null if not found or index is out of range.
         */
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

        /**
         * Retrieves a specific ingredient by index (1-20).
         *
         * @param i The index of the ingredient to retrieve.
         * @return The ingredient string, or null if not found or index is out of range.
         */
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

        fun toMeal() = Meal(
            id = id,
            name = name,
            imageUrl = imageUrl,
            area = area
        )
    }


}
