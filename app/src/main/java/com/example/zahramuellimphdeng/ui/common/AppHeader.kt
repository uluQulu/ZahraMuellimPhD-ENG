package com.example.zahramuellimphdeng.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

// Define your custom font family here.
// IMPORTANT: Change 'R.font.montserrat_regular' to match the filename you added.
val customFontFamily = FontFamily(Font(R.font.cherry_bomb_one_regular))

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier.padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Zəhra Seyid PhD ENG",
            fontFamily = customFontFamily, // Using the custom font
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_placeholder),
            contentDescription = "App Logo",
            modifier = Modifier.height(50.dp) // Slightly smaller logo
        )
    }
}