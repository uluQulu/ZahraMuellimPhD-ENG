package com.example.zahramuellimphdeng.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// A sealed class to represent our navigation destinations
sealed class NavItem(val route: String, val label: String, val icon: ImageVector) {
    // Bottom Bar Items
    object Fill : NavItem("fill_the_blank", "Fill", Icons.Default.Edit)
    object Choice : NavItem("multiple_choice", "Choice", Icons.Default.CheckCircle)
    object Match : NavItem("matching_game", "Match", Icons.Default.CompareArrows)
    object Meaning : NavItem("meaning_quiz", "Meaning", Icons.Default.Translate)

    // Sidebar (Drawer) Items
    object Account : NavItem("account", "Account", Icons.Outlined.AccountCircle)
    object History : NavItem("history", "History", Icons.Outlined.History)
    object Settings : NavItem("settings", "Settings", Icons.Outlined.Settings)
}