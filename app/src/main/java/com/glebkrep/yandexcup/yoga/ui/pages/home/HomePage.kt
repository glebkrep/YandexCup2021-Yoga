package com.glebkrep.yandexcup.yoga.ui.pages.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glebkrep.yandexcup.yoga.ui.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePage(outterNavController: NavController) {
    val recordAudioPermissionState =
        rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    Column(Modifier.fillMaxSize()) {

        PermissionRequired(
            permissionState = recordAudioPermissionState,
            permissionNotGrantedContent = {
                Column {
                    Text("Для работы приложения нужно разрешение на запись аудио")
                    Row {
                        Button(onClick = { recordAudioPermissionState.launchPermissionRequest() }) {
                            Text("Предоставить разрешение!")
                        }
                    }
                }
            },
            permissionNotAvailableContent = {
                Column {
                    Text(
                        "Разрешение не было предоставлено, приложение не сможет работать..."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        ) {
            Text("Разрешение предоставлено")
            Button(onClick = {
                outterNavController.navigate(Screen.BreathingPage.route)
            }) {
                Text(text = "Начать тренировку (запись дыхания)")
            }
        }

        Button(onClick = {
            outterNavController.navigate(Screen.StatsList.route)
        }) {
            Text(text = "Посмотреть записи (и отправить)")
        }
    }
}