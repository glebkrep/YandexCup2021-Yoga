package com.glebkrep.yandexcup.yoga.ui.pages.sendData

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.yoga.ui.theme.UiConsts
import com.glebkrep.yandexcup.yoga.utils.getActivity
import com.glebkrep.yandexcup.yoga.utils.isValidEmail

@Composable
fun SendDataPage(sendDataVM: SendDataPageVM = viewModel()) {
    var email by remember { mutableStateOf("") }
    var isError by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current


    Column(Modifier.fillMaxSize()) {
        Text(text = "Введите почту, на которую хотите отправить отчет:",Modifier.padding(UiConsts.padding))
        TextField(value = email, onValueChange = {
            isError = false
            email = it
        }, label = {
            Text(text = "Почта")
        },modifier = Modifier.padding(UiConsts.padding))
        if (isError) {
            Text(text = "Некорректный адрес электронной почты", color = Color.Red, modifier =Modifier.padding(UiConsts.padding))
        }

        Button(onClick = {
            if (email.isValidEmail()) {
                sendDataVM.sendEmail(
                    email,
                    context.getActivity() ?: throw Exception("Activtiy cant be null")
                )
            } else {
                isError = true
            }

        },Modifier.padding(UiConsts.padding)) {
            Text(text = "Отправить")
        }
    }
}