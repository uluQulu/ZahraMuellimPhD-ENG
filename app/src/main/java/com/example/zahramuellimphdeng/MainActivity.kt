package com.example.zahramuellimphdeng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zahramuellimphdeng.ui.MainViewModel
import com.example.zahramuellimphdeng.ui.NavItem
import com.example.zahramuellimphdeng.ui.screens.*
import com.example.zahramuellimphdeng.ui.theme.ZahraMuellimPhDENGTheme
import com.example.zahramuellimphdeng.utils.SoundPlayer

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // **FIX #1: Initialize the SoundPlayer when the app starts**
        SoundPlayer.initialize(applicationContext)

        setContent {
            ZahraMuellimPhDENGTheme {
                val navController = rememberNavController()
                val navItems = listOf(NavItem.Fill, NavItem.Choice, NavItem.Match, NavItem.Meaning)

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController, items = navItems)
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigationHost(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }

    // **FIX #2: Release the SoundPlayer resources when the app is closed**
    override fun onDestroy() {
        super.onDestroy()
        SoundPlayer.release()
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, items: List<NavItem>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavigationHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = NavItem.Fill.route) {
        composable(NavItem.Fill.route) {
            FillTheBlankScreen(viewModel = viewModel)
        }
        composable(NavItem.Choice.route) {
            MultipleChoiceScreen(viewModel = viewModel)
        }
        composable(NavItem.Match.route) {
            MatchingGameScreen(viewModel = viewModel)
        }
        composable(NavItem.Meaning.route) {
            MeaningQuizScreen(viewModel = viewModel)
        }
    }
}