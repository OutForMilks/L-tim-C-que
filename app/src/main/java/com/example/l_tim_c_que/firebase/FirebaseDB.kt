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
import java.util.Date

object FirebaseDB {
    lateinit var auth: FirebaseAuth
    @SuppressLint("StaticFieldLeak")
    lateinit var firestore: FirebaseFirestore
    val TAG = "FirebaseDB"
    val settings = firestoreSettings {
        setLocalCacheSettings(persistentCacheSettings {})
    }
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

    fun getAllBookmarks(onComplete: (List<APIModel.MealDetail>) -> Unit) {
        firestore.collection("bookmarks")
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<APIModel.MealDetail>()
                result.forEach{ a -> list.add(a.toObject(FirebaseModels.Bookmark::class.java).recipe)}
                onComplete(list)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun getAllRecent(onComplete: (List<APIModel.MealDetail>) -> Unit) {
        firestore.collection("recent")
            .whereEqualTo("user_id", currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<APIModel.MealDetail>()
                result.forEach{ a -> list.add(a.toObject(FirebaseModels.Recent::class.java).recipe)}
                onComplete(list)
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }
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