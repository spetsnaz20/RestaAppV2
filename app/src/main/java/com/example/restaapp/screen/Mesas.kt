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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun Mesas(navController: NavController){
    MostrarMesas(navController)
}

@Composable
fun MostrarMesas(navController: NavController){
    val mesasDisponibles = remember{ mutableStateOf(7) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        for (i in 1..mesasDisponibles.value){
            Button(onClick = {
                navController.navigate("mesa/${i}")
            },modifier = Modifier
                .height(70.dp)
                .width(200.dp), colors = ButtonDefaults.buttonColors(containerColor= Color.Black)) {
                Text(text = "Mesa ${i}",fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier =Modifier.height(20.dp))
        }
    }
}