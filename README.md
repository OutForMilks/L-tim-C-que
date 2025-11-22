# MOBICOM-MCO3

## Overview
Letim Coque is a reciper finder application designed to help users find and manage recipes. Users can search for recipes filtered by name, country of origin, or ingredient. They can also discover new recipes using a random recipe generator for spontaneous meal ideas.

The app also supports local storage for saving bookmarked recipes and tracking recently viewed recipes. This allows for offline access to these items to enhance user experience.

It primarily relies on **TheMealDB API**, which serves as the source for live recipe data, and **SQLite**, which is used to store user-specific-data locally.

## Group Members
* **Borja, Stephen**
* **Pua, Daniel**
* **Reyes, Jericho**

## Features
* **Search Functionality:** Search for recipes filtered by name, ingredient, or country (e.g., "Japanese").
* **Recipe Details:** View essential recipe details including ingredients, measurements, and step-by-step instructions.
* **Random Recipe:** Generate a randomly featured recipe suggestion.
* **Bookmarks:** Save favorite recipes for fast and offline access.
* **Recently Viewed:** Tracks a history of recently viewed recipes.
* **Offline Access:** Bookmarked and recently viewed recipes are locally cached and accessible even without an internet connection.

## Built With
* **Language:** Kotlin
* **Data:** [TheMealDB API](https://www.themealdb.com/api.php) (JSON)
* **Local Storage:** SQLite
* **UI Components:**
    * RecyclerView for listing recipes.
    * Fragments for navigation (Home, Search, Random, Bookmark).

## Installation and Setup
1.  Clone the repository:
    ```bash
    git clone (https://github.com/OutForMilks/L-tim-C-que)
    ```
2.  Open the project in **Android Studio**.
3.  Sync the project with Gradle files to download dependencies (App uses Gradle version 8.13).
4.  Run the application on an Android Emulator or physical device (API level 24 or higher recommended).
