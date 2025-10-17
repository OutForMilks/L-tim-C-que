package com.example.l_tim_c_que

object RecipeSamples {

    fun get(): ArrayList<Recipe> {
        val chickenMushroomHotpot = Recipe(
            id = "52795",
            name = "Chicken & Mushroom Hotpot",
            category = "Chicken",
            area = "British",
            instructions = "1. Heat oven to 200C/180C fan/gas 6... 2. Add the chicken... 3. Stir in the mushrooms...",
            imageUrl = "https://www.themealdb.com/images/media/meals/uuuspp1511297945.jpg",
            ingredients = listOf("1 tbsp Olive Oil", "2 large Chicken Breasts", "150g Mushrooms"),
            isBookmarked = true
        )

        val mushroomChestnutRotolo = Recipe(
            id = "52814",
            name = "Mushroom & Chestnut Rotolo",
            category = "Vegetarian",
            area = "British",
            instructions = "1. For the rotolo, soak the porcini mushrooms... 2. Meanwhile, heat the butter and oil...",
            imageUrl = "https://www.themealdb.com/images/media/meals/ssyqwr1511451678.jpg",
            ingredients = listOf("40g Dried Porcini Mushrooms", "100g Butter", "2 tbsp Olive Oil"),
            isBookmarked = false
        )

        val jerkChicken = Recipe(
            id = "52822",
            name = "Jerk chicken with rice & peas",
            category = "Chicken",
            area = "British",
            instructions = "1. For the jerk marinade, blitz the ingredients in a food processor...",
            imageUrl = "https://www.themealdb.com/images/media/meals/tytyxu1515363282.jpg",
            ingredients = listOf("3 Scallions", "2 cloves Garlic", "1 chunk Ginger"),
            isBookmarked = false
        )

        val sushi = Recipe(
            id = "53065",
            name = "Sushi",
            category = "Seafood",
            area = "Japanese",
            instructions = "1. PREPARE THE SUSHI RICE. 2. PREPARE THE FILLINGS. 3. ROLL THE SUSHI...",
            imageUrl = "https://www.themealdb.com/images/media/meals/g046bb1663960946.jpg",
            ingredients = listOf("1 cup Sushi Rice", "1 sheet Nori", "1/2 Avocado"),
            isBookmarked = true
        )

        val mangoFloat = Recipe(
            id = "53043",
            name = "Mango Float",
            category = "Dessert",
            area = "Filipino",
            instructions = "1. In a bowl, combine condensed milk and all-purpose cream... 2. Arrange a layer of graham crackers...",
            imageUrl = "https://www.themealdb.com/images/media/meals/ryppsv1511799741.jpg",
            ingredients = listOf("3 large Mangoes", "1 can Condensed Milk", "1 pack All-Purpose Cream"),
            isBookmarked = true
        )

        val spaghettiCarbonara = Recipe(
            id = "52772",
            name = "Spaghetti Carbonara",
            category = "Pasta",
            area = "Italian",
            instructions = "1. Cook the pasta. 2. Fry the pancetta. 3. Whisk eggs and cheese. 4. Combine and serve.",
            imageUrl = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
            ingredients = listOf("200g Spaghetti", "100g Pancetta", "2 large Eggs", "50g Pecorino Cheese"),
            isBookmarked = false
        )

        val beefWellington = Recipe(
            id = "52803",
            name = "Beef Wellington",
            category = "Beef",
            area = "British",
            instructions = "1. Sear the beef fillet. 2. Prepare the duxelles. 3. Wrap in pastry. 4. Bake until golden brown.",
            imageUrl = "https://www.themealdb.com/images/media/meals/vvpprx1487325699.jpg",
            ingredients = listOf("400g Beef Fillet", "250g Chestnut Mushroom", "1 packet Puff Pastry"),
            isBookmarked = false
        )

        val tandooriChicken = Recipe(
            id = "52805",
            name = "Tandoori Chicken",
            category = "Chicken",
            area = "Indian",
            instructions = "1. Marinate the chicken in yogurt and spices. 2. Let it sit for at least 4 hours. 3. Grill or bake.",
            imageUrl = "https://www.themealdb.com/images/media/meals/qptpvt1487339892.jpg",
            ingredients = listOf("4 Chicken Thighs", "1 cup Plain Yogurt", "1 tbsp Tandoori Masala"),
            isBookmarked = true
        )

        val fishAndChips = Recipe(
            id = "52807",
            name = "Fish and Chips",
            category = "Seafood",
            area = "British",
            instructions = "1. Prepare the batter. 2. Coat the fish fillets. 3. Deep-fry. 4. Serve with chips.",
            imageUrl = "https://www.themealdb.com/images/media/meals/ysxwuq1487323065.jpg",
            ingredients = listOf("500g Cod Fillets", "1 cup All-Purpose Flour", "1 tsp Baking Powder"),
            isBookmarked = false
        )

        val padThai = Recipe(
            id = "52802",
            name = "Pad Thai",
            category = "Vegetarian",
            area = "Thai",
            instructions = "1. Soak rice noodles. 2. Prepare sauce. 3. Stir-fry everything. 4. Garnish and serve.",
            imageUrl = "https://www.themealdb.com/images/media/meals/wokrqw1511297545.jpg",
            ingredients = listOf("200g Rice Noodles", "1 block Tofu", "3 tbsp Tamarind Paste"),
            isBookmarked = false
        )

        return arrayListOf(
            chickenMushroomHotpot,
            mushroomChestnutRotolo,
            jerkChicken,
            sushi,
            mangoFloat,
            spaghettiCarbonara,
            beefWellington,
            tandooriChicken,
            fishAndChips,
            padThai
        )
    }
}