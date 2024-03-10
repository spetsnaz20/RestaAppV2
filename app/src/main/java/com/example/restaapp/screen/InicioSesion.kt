package com.example.restaapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.restaapp.database.LoginScreenViewModel
import com.example.restaapp.navigation.Routes
import com.google.firebase.auth.FirebaseAuth


@Composable
fun InicioSesion(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    if(auth.currentUser != null){
        navController.navigate(Routes.Mesas.route)
    }
    else{
        Cuerpo(navController)
    }
}

@Composable
fun Cuerpo(navController: NavController){
    var text by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        email(text){newText ->
            text = newText
        }
        Spacer(modifier = Modifier.height(20.dp))
        password(password){newPassword ->
            password = newPassword
        }
        Spacer(modifier = Modifier.height(25.dp))
        btnInicioSesion(text,password,navController)
    }
}

@Composable
fun email(text: String, onTextChange: (String) -> Unit){
    Text(text = "Email", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(5.dp))
    TextField(
        value = text,
        onValueChange = { onTextChange(it) },
    )
}

@Composable
fun password(password: String, onPasswordChange: (String) -> Unit){
    var passwordVisibility = remember { mutableStateOf(false) }

    Text(text = "Password", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(5.dp))
    TextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        )
}

@Composable
fun btnInicioSesion(text: String, password: String,navController: NavController,viewModel: LoginScreenViewModel = viewModel()){
    var context = LocalContext.current
    Button(onClick = {
        viewModel.signInWithEmailAndPassword(text,password,context){
            navController.navigate(Routes.Mesas.route)
        }
    }, modifier = Modifier
        .height(70.dp)
        .width(200.dp), colors = ButtonDefaults.buttonColors(containerColor= Color.Black)) {
        Text(text = "Iniciar sesion", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
    }
}