package com.example.l_tim_c_que.firebase

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.example.l_tim_c_que.api.APIModel
// import com.example.l_tim_c_que.Recipe
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import java.util.Date

object FirebaseDB {
    val auth = Firebase.auth
    @SuppressLint("StaticFieldLeak")
    val firestore = Firebase.firestore
    val settings = firestoreSettings {
        // Use memory cache
        setLocalCacheSettings(memoryCacheSettings {})
        // Use persistent disk cache (default)
        setLocalCacheSettings(persistentCacheSettings {})
    }
    init {
        firestore.firestoreSettings = settings
    }
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    fun signIn(onComplete: ((Boolean) -> Unit)? = null) {
        if(currentUser != null) {
            onComplete?.invoke(true)
            return
        }
        else {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInAnonymously:success")
                        onComplete?.invoke(true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
                        onComplete?.invoke(false)
                    }
                }
        }
    }

    fun saveBookmark(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit): Unit {
        if(currentUser == null) {
            Log.w(TAG, "No current user. Cannot save bookmark.")
            onComplete.invoke(false)
            return
        }
        val bookmark = hashMapOf(
            "userID" to currentUser!!.uid,
            "recipe" to meal
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

    fun saveRecent(meal: APIModel.MealDetail, onComplete: (Boolean) -> Unit): Unit {
        if(currentUser == null) {
            Log.w(TAG, "No current user. Recently viewed not updated.")
            onComplete.invoke(false)
            return
        }

        val count = firestore.collection("recent").count().get(AggregateSource.SERVER).result.count
        // check if recent is equal to 4, delete excess if yes
        if(count >= 4) {
            val overflow = count - 4
            deleteOldest(overflow)
        }

        val recent = hashMapOf(
            "userID" to currentUser!!.uid,
            "recipe" to meal,
            "date" to Timestamp(Date())
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
    }

    fun deleteOldest(overflow: Long) {
        firestore.collection("recent")
            .orderBy("Timestamp", Query.Direction.ASCENDING)
            .limit(1)
            .get()
            .result
            .documents
            .forEach {
                mapEntry -> {
                    val id = mapEntry.id
                    firestore.collection("recent").document(id).delete()
                }
            }
    }
}