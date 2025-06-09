package com.example.zahramuellimphdeng.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Mistakes", "Correct Results")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }
        // Content for the selected tab
        when (selectedTabIndex) {
            0 -> HistoryContent(type = "Mistakes")
            1 -> HistoryContent(type = "Correct Results")
        }
    }
}

@Composable
fun HistoryContent(type: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "History for $type (Placeholder)")
    }
}