package com.glebkrep.yandexcup.yoga.ui.pages.sendData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.yoga.utils.getActivity

@Composable
fun SendDataPage(sendDataVM: SendDataPageVM = viewModel()) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current


    Column(Modifier.fillMaxSize()) {

        Text(text = "Enter recepient email:")
        //todo add validation
        TextField(value = email, onValueChange = {
            email = it
        })

        Button(onClick = {
            sendDataVM.sendEmail(
                email,
                context.getActivity() ?: throw Exception("Activtiy cant be null")
            )
        }) {
            Text(text = "Отправить")
        }
    }
}