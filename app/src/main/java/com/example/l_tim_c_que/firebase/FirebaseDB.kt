package com.example.l_tim_c_que.firebase

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.example.l_tim_c_que.api.APIModel
// import com.example.l_tim_c_que.Recipe
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

object FirebaseDB {
    val auth = Firebase.auth
    @SuppressLint("StaticFieldLeak")
    val firestore = Firebase.firestore
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
}