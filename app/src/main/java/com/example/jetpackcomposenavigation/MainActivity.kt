package com.example.jetpackcomposenavigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.jetpackcomposenavigation.MainDestinations.CHILDSCREEN
import com.example.jetpackcomposenavigation.MainDestinations.CHILD_TITLE_KEY
import com.example.jetpackcomposenavigation.MainDestinations.MAINSCREEN
import com.example.jetpackcomposenavigation.ui.theme.JetpackComposeNavigationTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeNavigationTheme {
                NavGraph()
            }
        }
    }
}

object MainDestinations {
    const val MAINSCREEN = "mainscreen"
    const val CHILDSCREEN = "childscreen"
    const val CHILD_TITLE_KEY = "childscreen_titletkey"
}

@Composable
fun NavGraph(startDestination: String = MAINSCREEN) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MAINSCREEN) {
            MainScreen(actions)
        }
        composable(
            "$CHILDSCREEN/{$CHILD_TITLE_KEY}",
            arguments = listOf(navArgument(CHILD_TITLE_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            ChildScreen(arguments.getString(CHILD_TITLE_KEY), actions)
        }
    }
}

class MainActions(navController: NavHostController) {
    val mainScreen: () -> Unit = {
        navController.navigate(MAINSCREEN)
    }
    val childScreen: (String) -> Unit = { title ->
        navController.navigate("$CHILDSCREEN/$title")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}

@Composable
fun MainScreen(actions: MainActions) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            MyButton("Child Screen 1", actions)
            MyButton("Child Screen 2", actions)
        }
    }
}

@Composable
fun ColumnScope.MyButton(title: String, actions: MainActions) {
    Button(
        onClick = { actions.childScreen(title) },
        modifier = Modifier
            .weight(1f)
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Text(title)
    }
}

@Composable
fun ChildScreen(title: String?, actions: MainActions) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = actions.upPress,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(title ?: "No title")
        }
    }
}
