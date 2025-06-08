package com.example.zahramuellimphdeng.ui

import androidx.compose.material.icons.Icons
// IMPORTANT: Make sure this import line is exactly as written below
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// A sealed class to represent our navigation destinations
sealed class NavItem(val route: String, val label: String, val icon: ImageVector) {
    object Fill : NavItem("fill_the_blank", "Fill", Icons.Default.Edit)
    object Choice : NavItem("multiple_choice", "Choice", Icons.Default.CheckCircle)

    // *** THE FIX IS HERE: We are using a different, guaranteed icon ***
    object Match : NavItem("matching_game", "Match", Icons.Default.CompareArrows)

    object Meaning : NavItem("meaning_quiz", "Meaning", Icons.Default.Translate)
}