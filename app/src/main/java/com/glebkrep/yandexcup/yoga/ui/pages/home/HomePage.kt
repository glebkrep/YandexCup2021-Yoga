package com.glebkrep.yandexcup.yoga.ui.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.glebkrep.yandexcup.yoga.R
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
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        PermissionRequired(
            permissionState = recordAudioPermissionState,
            permissionNotGrantedContent = {
                Text(
                    stringResource(R.string.permission_explanation),
                    Modifier.padding(UiConsts.padding),
                    textAlign = TextAlign.Center
                )
                Row {
                    Button(
                        onClick = { recordAudioPermissionState.launchPermissionRequest() },
                        Modifier.padding(UiConsts.padding)
                    ) {
                        Text(stringResource(R.string.request_permission))
                    }
                }
            },
            permissionNotAvailableContent = {
                Text(
                    stringResource(R.string.permission_fail),
                    Modifier.padding(UiConsts.padding)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        ) {
            Button(onClick = {
                outterNavController.navigate(Screen.BreathingPage.route)
            }, Modifier.padding(UiConsts.padding)) {
                Text(text = stringResource(R.string.start_activity))
            }
        }

        Button(onClick = {
            outterNavController.navigate(Screen.StatsList.route)
        }, Modifier.padding(UiConsts.padding)) {
            Text(text = stringResource(R.string.look_into_records))
        }
    }
}