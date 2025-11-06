package ru.topbun.pawmate.presentation

import android.Manifest
import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.topbun.pawmate.R
import ru.topbun.pawmate.entity.Reminder
import ru.topbun.pawmate.presentation.screens.auth.AuthScreen
import ru.topbun.pawmate.presentation.theme.Colors
import ru.topbun.pawmate.presentation.theme.colorScheme
import java.util.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme) {
                Box(Modifier.statusBarsPadding()){
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setStatusBarColor(Colors.BROWN, true)
                    requestPermissionNotify()
                    Navigator(AuthScreen){
                        FadeTransition(it, animationSpec = tween(200))
                    }
                }
            }
        }
    }

    @Composable
    private fun requestPermissionNotify(){
        val contract = ActivityResultContracts.RequestMultiplePermissions()
        val launcher = rememberLauncherForActivityResult(contract = contract) {
        }
        SideEffect {
            launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.USE_EXACT_ALARM, Manifest.permission.SCHEDULE_EXACT_ALARM) )
        }
    }


}