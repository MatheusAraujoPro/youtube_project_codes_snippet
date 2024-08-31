package com.example.jetpack_compose_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.LocalContext
import com.example.jetpack_compose_app.ui.theme.Jetpack_compose_appTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WidgetViewModel = WidgetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jetpack_compose_appTheme {
                // A surface container using the 'background' color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.checkForPermission(LocalContext.current)
                }
            }
        }
    }
}

@Composable
fun TestScreen() {
    Column {

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Jetpack_compose_appTheme {
        TestScreen()
    }
}
