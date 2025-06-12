package com.example.zahramuellimphdeng.ui.screens.nouns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zahramuellimphdeng.data.Noun
import com.example.zahramuellimphdeng.ui.MainViewModel
import com.example.zahramuellimphdeng.utils.SoundPlayer
import com.example.zahramuellimphdeng.utils.TTSPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MatchThePairNouns(viewModel: MainViewModel) {
    val allNouns = remember { viewModel.allNouns.shuffled().take(7) }
    val englishWords = remember { allNouns.map { it.noun_en }.shuffled() }
    val azerbaijaniWords = remember { allNouns.map { it.translation_az }.shuffled() }

    var selectedEnglish by remember { mutableStateOf<String?>(null) }
    var matchedPairs by remember { mutableStateOf(setOf<Pair<String, String>>()) }
    var score by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Match the Noun Pairs", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Score: $score", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // English Words
            Column {
                Text("English", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                englishWords.forEach { word ->
                    val isMatched = matchedPairs.any { it.first == word }
                    Button(
                        onClick = {
                            SoundPlayer.playClickSound()
                            selectedEnglish = if (selectedEnglish == word) null else word
                            TTSPlayer.speak(word)
                        },
                        enabled = !isMatched,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedEnglish == word) Color(0xFF90CAF9) else MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(word)
                    }
                }
            }

            // Azerbaijani Words
            Column {
                Text("Azerbaijani", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                azerbaijaniWords.forEach { translation ->
                    val isMatched = matchedPairs.any { it.second == translation }
                    Button(
                        onClick = {
                            SoundPlayer.playClickSound()
                            val correctPair = allNouns.find { it.noun_en == selectedEnglish && it.translation_az == translation }

                            if (selectedEnglish != null && correctPair != null) {
                                matchedPairs = matchedPairs + Pair(correctPair.noun_en, correctPair.translation_az)
                                score++
                                selectedEnglish = null

                                coroutineScope.launch {
                                    delay(200)
                                    TTSPlayer.speak(correctPair.noun_en)
                                }
                            } else {
                                SoundPlayer.playWrongSound()
                                selectedEnglish = null
                            }
                        },
                        enabled = !isMatched,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMatched) Color.Gray else MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(translation, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (matchedPairs.size == allNouns.size) {
            Text("🎉 All pairs matched! Great job!", color = Color(0xFF4CAF50), fontSize = 18.sp)
        }
    }
}
