package com.example.restaapp.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.  auth

    fun signInWithEmailAndPassword(email: String, password:String, context: Context, home: ()-> Unit)
            = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(context, "Credenciales correctas.", Toast.LENGTH_SHORT).show()
                        home()
                    }
                    else{
                        Toast.makeText(context, "Credenciales incorrectas. ", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        catch (ex: Exception){
            Log.d("App Login","${ex.message}")
        }
    }

}