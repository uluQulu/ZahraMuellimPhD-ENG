package com.example.zahramuellimphdeng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.zahramuellimphdeng.ui.MainViewModel
import com.example.zahramuellimphdeng.ui.NavItem
import com.example.zahramuellimphdeng.ui.common.AppHeader
import com.example.zahramuellimphdeng.ui.screens.*
import com.example.zahramuellimphdeng.ui.screens.irregularverbs.*
import com.example.zahramuellimphdeng.ui.screens.nouns.*
import com.example.zahramuellimphdeng.ui.theme.ZahraMuellimPhDENGTheme
import com.example.zahramuellimphdeng.utils.SoundPlayer
import com.example.zahramuellimphdeng.utils.TTSPlayer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundPlayer.initialize(applicationContext)
        TTSPlayer.initialize(applicationContext)

        setContent {
            ZahraMuellimPhDENGTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                var selectedCategory by remember { mutableStateOf("verbs") }

                val bottomNavItems = when (selectedCategory) {
                    "verbs" -> listOf(NavItem.Fill, NavItem.Choice, NavItem.Match, NavItem.Meaning)
                    "nouns" -> listOf(NavItem.FillGN, NavItem.MatchPN, NavItem.ChooseWN, NavItem.AntonymN)
                    else -> emptyList()
                }
                val drawerNavItems = listOf(NavItem.Account, NavItem.History, NavItem.Settings)

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Zəhra Seyid",
                                    modifier = Modifier.padding(start = 16.dp),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            drawerNavItems.forEach { item ->
                                NavigationDrawerItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(item.label) },
                                    selected = false,
                                    onClick = {
                                        coroutineScope.launch { drawerState.close() }
                                        navController.navigate(item.route)
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Made by Shahriyar Guliyev",
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "2025 Nakhchivan",
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            Column {
                                AppHeader(onMenuClick = {
                                    coroutineScope.launch { drawerState.open() }
                                })

                                // --- TOP BUTTON ROW (Category Switch) ---
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = {
                                            selectedCategory = "verbs"
                                            navController.navigate(NavItem.Fill.route) {
                                                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedCategory == "verbs") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text("Irregular Verbs")
                                    }

                                    Button(
                                        onClick = {
                                            selectedCategory = "nouns"
                                            navController.navigate(NavItem.FillGN.route) {
                                                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedCategory == "nouns") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text("Nouns")
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            if (bottomNavItems.any { it.route == currentRoute }) {
                                BottomNavigationBar(navController = navController, items = bottomNavItems)
                            }
                        }
                    ) { innerPadding ->
                        AppNavigationHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayer.release()
        TTSPlayer.release()
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
fun AppNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController,
        startDestination = NavItem.Fill.route,
        modifier = modifier
    ) {
        // Irregular Verbs
        composable(NavItem.Fill.route) { FillTheBlankScreen(viewModel = viewModel) }
        composable(NavItem.Choice.route) { MultipleChoiceScreen(viewModel = viewModel) }
        composable(NavItem.Match.route) { MatchingGameScreen(viewModel = viewModel) }
        composable(NavItem.Meaning.route) { MeaningQuizScreen(viewModel = viewModel) }

        // Nouns
        composable(NavItem.FillGN.route) { FillTheGapNouns(viewModel = viewModel) }
        composable(NavItem.MatchPN.route) { MatchThePairNouns(viewModel = viewModel) }
        composable(NavItem.ChooseWN.route) { ChooseTheWordNouns(viewModel = viewModel) }
        composable(NavItem.AntonymN.route) { AntonymNouns(viewModel = viewModel) }

        // Drawer Items
        composable(NavItem.Account.route) { AccountScreen() }
        composable(NavItem.History.route) { HistoryScreen() }
        composable(NavItem.Settings.route) { SettingsScreen() }
    }
}
