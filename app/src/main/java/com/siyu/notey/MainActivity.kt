
package com.siyu.notey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import kotlin.getValue
import com.siyu.notey.data.local.TaskDataStore
import com.siyu.notey.data.repository.TaskRepositoryImpl
import com.siyu.notey.presentation.navigation.AppNav
import com.siyu.notey.presentation.viewmodel.TaskViewModel
import com.siyu.notey.presentation.viewmodel.TaskViewModelFactory
import com.siyu.notey.ui.theme.NoteyTheme

class MainActivity : ComponentActivity() {


    //by viewModels crea el ViewModel una sola vez y lo conserva al girar la pantalla. Después AppNav(vm = taskViewModel) lo reparte a todas las pantallas, de modo que comparten el mismo estado.
    //se usaa applicationContext y no this para el TaskDataStore, así el DataStore no retiene la Activity.
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepositoryImpl(TaskDataStore(applicationContext))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteyTheme {
                AppNav(vm = taskViewModel)
            }
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
    }
}
