package com.example.zahramuellimphdeng.ui.screens.nouns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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

@Composable
fun AntonymNouns(viewModel: MainViewModel) {
    val allNouns = viewModel.allNouns.filter { it.antonyms.isNotEmpty() && it.synonyms.isNotEmpty() }

    var currentNoun by remember { mutableStateOf(allNouns.random()) }
    var options by remember { mutableStateOf(generateOptions(currentNoun)) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    fun checkAnswer() {
        if (selectedOption == currentNoun.antonyms.first()) {
            SoundPlayer.playCorrectSound()
            feedback = "Correct! 🎉"
            score++
        } else {
            SoundPlayer.playWrongSound()
            feedback = "Incorrect. Correct answer: ${currentNoun.antonyms.first()}"
        }
    }

    fun nextQuestion() {
        val newNoun = allNouns.shuffled().first()
        currentNoun = newNoun
        options = generateOptions(newNoun)
        selectedOption = null
        feedback = null
    }

    val wordDisplay = listOfNotNull(
        currentNoun.article?.takeIf { it.isNotBlank() && it.lowercase() != "null" },
        currentNoun.noun_en
    ).joinToString(" ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose the antonym", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

//        Text("Word:", fontSize = 16.sp, color = Color.Gray)
        Text(
            text = wordDisplay,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { TTSPlayer.speak(currentNoun.noun_en) }
        )
        Text(
            text = "${currentNoun.translation_az}",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        options.forEach { option ->
            val onSelect = {
                TTSPlayer.speak(option)
                SoundPlayer.playClickSound()
                selectedOption = option
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedOption == option),
                        onClick = onSelect,
                        enabled = feedback == null
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedOption == option),
                    onClick = onSelect,
                    enabled = feedback == null
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        feedback?.let {
            val color = if (it.startsWith("Correct")) Color(0xFF4CAF50) else Color.Red
            Text(it, color = color, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    SoundPlayer.playClickSound()
                    checkAnswer()
                },
                enabled = selectedOption != null && feedback == null
            ) { Text("Check") }

            Button(onClick = {
                SoundPlayer.playClickSound()
                nextQuestion()
            }) { Text("Next") }
        }
    }
}

private fun generateOptions(currentNoun: Noun): List<String> {
    val correct = currentNoun.antonyms.first()
    val distractors = currentNoun.synonyms.shuffled().take(3)
    val allOptions = (distractors + correct).shuffled()
    return allOptions
}
