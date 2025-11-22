package com.example.l_tim_c_que.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.l_tim_c_que.api.APIModel
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Helper object for interacting with Firebase console
 */
object FirebaseDB {
    lateinit var auth: FirebaseAuth
    @SuppressLint("StaticFieldLeak")
    lateinit var firestore: FirebaseFirestore
    val TAG = "FirebaseDB" // for identifying in LogCat
    val settings = firestoreSettings {
        setLocalCacheSettings(persistentCacheSettings {})
    }

    /**
     * FirebaseUser containing the current user, done via anonymous sign-in
     * get() @returns the current user
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Signs in a user anonymously
     * @param onComplete boolean callback to check for successful login
     */
    fun signIn(onComplete: ((Boolean) -> Unit)? = null) {
        if(currentUser != null) {
            onComplete?.invoke(true)
            return
        }
        else {
            if (NetworkChecker.status.value)
                auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInAnonymously:success")
                            onComplete?.invoke(true)
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.exception)
                            onComplete?.invoke(false)
                        }
                }
            else onComplete?.invoke(false)
        }
    }

    /**
     * Returns all bookmarked meals tied to a user
     * @return list of MealDetail
     */
    suspend fun getAllBookmarks(): List<APIModel.MealDetail> {
        return try {
            val snapshot = firestore.collection("bookmarks")
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .await()
            snapshot.mapNotNull { it.toObject(FirebaseModels.Recent::class.java).recipe }
        } catch (e: Exception) {
            Log.w(TAG, "Fetch list of recently viewed failed", e)
            emptyList()
        }
    }

    /**
     * Returns all recently viewed meals tied to a user
     * @return list of MealDetail
     */
    suspend fun getAllRecent(): List<APIModel.MealDetail> {
        return try {
            val snapshot = firestore.collection("recent")
                .whereEqualTo("user_id", currentUser?.uid)
                .get()
                .await()

            snapshot.mapNotNull { it.toObject(FirebaseModels.Recent::class.java).recipe }
        } catch (e: Exception) {
            Log.w(TAG, "Fetch list of recently viewed failed", e)
            emptyList()
        }
    }

    /**
     * Returns a specific meal in the user's bookmark list based on the given ID
     * @param id the id of the meal
     * @return MealDetail referring to that id, or null
     */
    suspend fun getSpecificBookmark(id: String): APIModel.MealDetail? {
        return try {
            val snapshot = firestore.collection("bookmarks")
                .whereEqualTo("user_id", currentUser?.uid)
                .whereEqualTo("meal_id", id)
                .get()
                .await()

            snapshot.first().toObject(FirebaseModels.Recent::class.java).recipe
        } catch (e: Exception) {
            Log.w(TAG, "Fetch list of recently viewed failed", e)

            null
        }
    }

    /**
     * Returns a specific meal in the user's bookmark list based on the given ID
     * @param id the id of the meal
     * @return MealDetail referring to that id, or null
     */
    suspend fun getSpecificRecent(id: String): APIModel.MealDetail? {
        return try {
            val snapshot = firestore.collection("recent")
                .whereEqualTo("user_id", currentUser?.uid)
                .whereEqualTo("meal_id", id)
                .get()
                .await()

            snapshot.first().toObject(FirebaseModels.Recent::class.java).recipe
        } catch (e: Exception) {
            Log.w(TAG, "Fetch list of recently viewed failed", e)

            null
        }
    }

    /**
     * Saves a meal to the user's bookmarked list
     * @param meal the MealDetail to be saved
     * @param onComplete callback function with boolean for status
     */
    fun saveBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit): Unit {
        if(currentUser == null) {
            Log.w(TAG, "No current user. Cannot save bookmark.")
            onComplete.invoke(false)
            return
        }
        isInBookmarks(meal.id) { it ->
            if(it){
                Log.d(TAG, "Already in bookmarks")
                onComplete(true)
                return@isInBookmarks
            }
            val bookmark = FirebaseModels.Bookmark(
                currentUser!!.uid,meal.id, meal
            )
            firestore.collection("bookmarks")
                .add(bookmark)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    onComplete.invoke(true)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    onComplete.invoke(false)
                }
        }
    }

    /**
     * Updates a meal (if there is any difference between the DB and API) in the user's bookmarked list
     * @param meal the MealDetail to be updated
     * @param onComplete callback function
     */
    fun updateBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit) {
        if (currentUser == null) {
            Log.w(TAG, "No current user. Cannot update bookmark.")
            onComplete(false)
            return
        }

        firestore.collection("bookmarks")
            .whereEqualTo("meal_id", meal.id)
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val document = snapshot.documents.firstOrNull()

                if (document == null) {
                    Log.w(TAG, "Bookmark not found, cannot update.")
                    onComplete(false)
                    return@addOnSuccessListener
                }

                val existing = document.toObject(FirebaseModels.Bookmark::class.java)

                if (existing == null) {
                    onComplete(false)
                    return@addOnSuccessListener
                }

                if (existing.recipe == meal) {
                    Log.d(TAG, "No changes detected. Update skipped.")
                    onComplete(true)
                    return@addOnSuccessListener
                }

                firestore.collection("bookmarks")
                    .document(document.id)
                    .update("recipe", meal)
                    .addOnSuccessListener {
                        Log.d(TAG, "Bookmark updated successfully.")
                        onComplete(true)
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "Failed to update bookmark.", it)
                        onComplete(false)
                    }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error finding bookmark for update.", it)
                onComplete(false)
            }
    }


    /**
     * Saves a meal to the user's recently viewed list.
     * Also removes the oldest items if number of items
     * in list is greater than 4
     * @param meal the MealDetail to be saved
     * @param onComplete callback function with boolean for status
     */
    fun saveRecent(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit): Unit {
        if(currentUser == null) {
            Log.w(TAG, "No current user. Recently viewed not updated.")
            onComplete.invoke(false)
            return
        }

        // check if already in recently viewed
        isInRecent(meal.id) { it ->
            if (it){
                Log.d(TAG, "Already in recently viewed")
                onComplete(true)
                return@isInRecent
            }
            firestore.collection("recent")
                .whereEqualTo("user_id", currentUser!!.uid)
                .count().get(AggregateSource.SERVER).addOnSuccessListener { it ->
                // check if recent is equal to 4, delete excess if yes
                if(it.count >= 4) {
                    val overflow = it.count - 3
                    deleteOldest(overflow)
                }

                val recent = FirebaseModels.Recent(
                    currentUser!!.uid,meal.id, meal, Timestamp(Date())
                )

                firestore.collection("recent")
                    .add(recent)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        onComplete.invoke(true)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        onComplete.invoke(false)
                    }
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error getting count aggregate", e)
                onComplete.invoke(false)
            }
        }
    }

    /**
     * Removes a bookmark from the user's list of bookmarks
     * @param meal the MealDetail to remove
     * @param onComplete callback function with boolean for status
     */
    fun removeBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit): Unit {
        firestore.collection("bookmarks")
            .whereEqualTo("meal_id", meal.id)
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .addOnSuccessListener { it ->
                val document = it.documents.firstOrNull()
                if (document != null) {
                    firestore.collection("bookmarks")
                        .document(
                            document.id
                        ).delete().addOnCompleteListener {
                            Log.d(TAG, "Bookmark deletion of ${meal.name} success")
                            onComplete.invoke(true)
                        }
                } else onComplete.invoke(false)
            }.addOnFailureListener {
                Log.w(TAG, "Bookmark deletion of ${meal.name} failed.")
                onComplete.invoke(false)
            }
    }

    /**
     * Checks if meal id is in user's list of bookmarks
     * @param id meal id to check
     * @param callback contains boolean for status
     */
    fun isInBookmarks(id: String, callback: (Boolean) -> Unit) {
        firestore.collection("bookmarks")
            .whereEqualTo("meal_id", id)
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                callback(!result.isEmpty)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    /**
     * Checks if meal id is in user's list of recently viewed
     * and updates timestamp if so
     * @param id meal id to check
     * @param callback contains boolean for status
     */
    fun isInRecent(id: String, callback: (Boolean) -> Unit) {
        firestore.collection("recent")
            .whereEqualTo("meal_id", id)
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                callback(!result.isEmpty)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    /**
     * Deletes the oldest meals from the user list of recently viewed.
     * Uses a composite index in Firestore.
     * @param overflow the number of items to delete
     */
    fun deleteOldest(overflow: Long) {
        firestore.collection("recent")
            .whereEqualTo("user_id", currentUser!!.uid)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .limit(overflow)
            .get()
            .addOnSuccessListener { it ->
                it.documents.forEach { mapEntry ->
                    val id = mapEntry.id
                    firestore.collection("recent").document(id).delete()
                }
            }
    }

    /**
     * Initializes Firebase and calls signIn(). Also runs a coroutine to check for network changes.
     * @param context the context this was called in
     */
    fun init(context: Context) {
        NetworkChecker.init(context)
        FirebaseApp.initializeApp(context)

        this.auth = Firebase.auth
        this.firestore = Firebase.firestore
        firestore.firestoreSettings = settings

        CoroutineScope(Dispatchers.Default).launch {
            NetworkChecker.status.collect { online ->
                if (online) firestore.enableNetwork()
                else firestore.disableNetwork()
            }
        }

        signIn { it -> if (!it) Toast.makeText(
            context,
            "Anonymous Sign In failed. Please connect to the internet at least once before using the app.",
            Toast.LENGTH_LONG
        ).show() }
    }
}