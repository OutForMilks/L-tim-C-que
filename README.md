# L-tim-C-que

A recipe finder application for Android.

## Overview

Letim Coque is a recipe finder application designed to help users find and manage recipes. Users can search for recipes filtered by name, country of origin, or ingredient. They can also discover new recipes using a random recipe generator for spontaneous meal ideas.

The app also supports local storage for saving bookmarked recipes and tracking recently viewed recipes. This allows for offline access to these items to enhance user experience.

It primarily relies on TheMealDB API, which serves as the source for live recipe data, and Firebase, which is used to store bookmarks and recently viewed recipes.

## Features

### Core Recipe Finder
- **Search Functionality**: Search for recipes filtered by name, ingredient, or country.
- **Recipe Details**: View essential recipe details including ingredients, measurements, and step-by-step instructions.
- **Random Recipe**: Generate a randomly featured recipe suggestion.

### Other Features
- **Bookmarks**: Save favorite recipes for fast and offline access.
- **Recently Viewed**: Tracks a history of recently viewed recipes.
- **Offline Access**: Bookmarked and recently viewed recipes are locally cached and accessible even without an internet connection.

## Project Structure
```
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── l_tim_c_que
│   │               ├── api
│   │               ├── firebase
│   │               ├── repository
│   │               ├── ui
│   │               │   ├── adapter
│   │               │   ├── bookmark
│   │               │   ├── detail
│   │               │   ├── home
│   │               │   ├── random
│   │               │   └── search
│   │               ├── util
│   │               └── viewmodel
│   │
│   └── res
│       ├── color
│       ├── drawable
│       ├── font
│       ├── layout
│       ├── menu
│       ├── mipmap-anydpi-v26
│       ├── mipmap-hdpi
│       ├── mipmap-mdpi
│       ├── mipmap-xhdpi
│       ├── mipmap-xxhdpi
│       ├── mipmap-xxxhdpi
│       ├── navigation
│       ├── values
│       ├── values-night
│       └── xml
...
```

## Quick Start

### Prerequisites
- Android Studio
- Android Emulator or physical device (API 24+)

### Running the App
1.  **Clone the repository**:
    ```bash
    git clone https://github.com/OutForMilks/L-tim-C-que
    ```
2.  **Open in Android Studio**: Open the cloned project directory in Android Studio.
3.  **Sync Gradle**: Allow Gradle to sync and download the required dependencies.
4.  **Run**: Run the application on an Android Emulator or a physical device.

## Technology Stack

- **Language**: Kotlin
- **API**: TheMealDB API for recipe data
- **Database**: Firebase
- **UI Components**:
    - RecyclerView
    - Fragments

## Authors

- Stephen Borja
- Daniel Pua
- Jericho Reyes
