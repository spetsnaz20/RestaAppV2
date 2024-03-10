package com.example.restaapp.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetRacciones {
    suspend fun recuperarRacciones(): Map<String, Any>? {
        try {
            val db = Firebase.firestore
            val docRef = db.collection("elcruce").document("Racciones")
            val document = docRef.get().await()
            return if (document != null && document.exists()) {
                document.data?.filterValues { it is Map<*, *> }
            } else {
                null
            }
        }
        catch (ex: Exception){
            return null
        }
    }
}