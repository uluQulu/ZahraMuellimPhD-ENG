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
fun ChooseTheWordNouns(viewModel: MainViewModel) {
    val allNouns = viewModel.allNouns
    var currentNoun by remember { mutableStateOf(allNouns.random()) }
    var options by remember { mutableStateOf(generateMeaningOptions(currentNoun, allNouns)) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    fun checkAnswer() {
        if (selectedOption == currentNoun.translation_az) {
            SoundPlayer.playCorrectSound()
            feedback = "Correct!"
            score++
        } else {
            SoundPlayer.playWrongSound()
            feedback = "Incorrect. The answer is ${currentNoun.translation_az}."
        }
    }

    fun nextQuestion() {
        val newNoun = allNouns.random()
        currentNoun = newNoun
        options = generateMeaningOptions(newNoun, allNouns)
        selectedOption = null
        feedback = null
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose the Correct Meaning", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("What is the meaning of:", fontSize = 16.sp, color = Color.Gray)
        Text(
            text = currentNoun.noun_en,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { TTSPlayer.speak(currentNoun.noun_en) }
        )
        Spacer(modifier = Modifier.height(24.dp))

        options.forEach { option ->
            val onSelect = {
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
                    fontSize = 18.sp,
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

private fun generateMeaningOptions(correctNoun: Noun, allNouns: List<Noun>): List<String> {
    val incorrectNouns = allNouns.filter { it != correctNoun }.shuffled().take(3)
    val options = incorrectNouns.map { it.translation_az }.toMutableList()
    options.add(correctNoun.translation_az)
    return options.shuffled()
}
