package com.example.restaapp.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import io.grpc.Context
import kotlinx.coroutines.tasks.await

class GetBebidas {
    suspend fun recuperarBedias(): Map<String, Any>? {
        try {
            val db = Firebase.firestore
            val docRef = db.collection("elcruce").document("Bebidas")
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