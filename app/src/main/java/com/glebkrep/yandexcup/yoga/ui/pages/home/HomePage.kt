package com.glebkrep.yandexcup.yoga.ui.pages.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glebkrep.yandexcup.yoga.ui.Screen
import com.glebkrep.yandexcup.yoga.ui.theme.UiConsts
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage(outterNavController: NavController) {
    val recordAudioPermissionState =
        rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        PermissionRequired(
            permissionState = recordAudioPermissionState,
            permissionNotGrantedContent = {
                Text(
                    "Для работы приложения нужно разрешение на запись аудио",
                    Modifier.padding(UiConsts.padding),
                    textAlign = TextAlign.Center
                )
                Row {
                    Button(
                        onClick = { recordAudioPermissionState.launchPermissionRequest() },
                        Modifier.padding(UiConsts.padding)
                    ) {
                        Text("Предоставить разрешение!")
                    }
                }
            },
            permissionNotAvailableContent = {
                Text(
                    "Разрешение не было предоставлено, приложение не сможет работать...",
                    Modifier.padding(UiConsts.padding)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        ) {
            Button(onClick = {
                outterNavController.navigate(Screen.BreathingPage.route)
            }, Modifier.padding(UiConsts.padding)) {
                Text(text = "Начать тренировку (запись дыхания)")
            }
        }

        Button(onClick = {
            outterNavController.navigate(Screen.StatsList.route)
        }, Modifier.padding(UiConsts.padding)) {
            Text(text = "Посмотреть записи (и отправить)")
        }
    }
}