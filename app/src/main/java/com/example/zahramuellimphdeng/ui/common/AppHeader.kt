package com.example.zahramuellimphdeng.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zahramuellimphdeng.R

val customFontFamily = FontFamily(Font(R.font.cherry_bomb_one_regular))

@Composable
fun AppHeader(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp), // Symmetrical padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu Icon on the far left
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MenuOpen,
                contentDescription = "Open Menu",
                modifier = Modifier.size(32.dp)
            )
        }

        // A flexible spacer that pushes everything after it to the right
        Spacer(modifier = Modifier.weight(1f))

        // Text and Logo, now free from incorrect constraints
        Text(
            text = "Zəhra Seyid PhD ENG",
            fontFamily = customFontFamily,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_placeholder),
            contentDescription = "App Logo",
            // *** THIS WILL NOW WORK ***
            // Change this value to whatever you want. It is no longer constrained.
            modifier = Modifier.size(55.dp)
        )

        // Another flexible spacer to push everything before it to the left
        Spacer(modifier = Modifier.weight(1f))
    }
}