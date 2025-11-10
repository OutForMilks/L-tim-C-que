package com.example.l_tim_c_que

import com.example.l_tim_c_que.api.APIClient
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class MealApiTest {

    private val apiService = APIClient.api  // use your existing singleton instance

    @Test
    fun testGetMealByName() = runBlocking {
        // Try fetching meals by name
        val response = apiService.getMealByName("chicken")

        // Check that we actually got a response object
        assertNotNull("Response should not be null", response)

        val meals = response.meals
        println("✅ API returned ${meals?.size ?: 0} meals")

        // Print out some meal info
        meals?.forEach { meal ->
            println("🍗 ${meal.name ?: "Unnamed"} (ID: ${meal.id})")
        }

        // Assert that there is at least one meal returned
        assertTrue("Response should contain meals", !meals.isNullOrEmpty())
    }
}