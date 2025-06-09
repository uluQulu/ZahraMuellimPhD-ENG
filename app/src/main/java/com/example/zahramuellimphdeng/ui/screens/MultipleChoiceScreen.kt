package com.example.zahramuellimphdeng.ui.screens

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
import com.example.zahramuellimphdeng.data.Verb
import com.example.zahramuellimphdeng.ui.MainViewModel
import com.example.zahramuellimphdeng.ui.common.AppHeader
import com.example.zahramuellimphdeng.utils.SoundPlayer
import com.example.zahramuellimphdeng.utils.TTSPlayer

@Composable
fun MultipleChoiceScreen(viewModel: MainViewModel) {
    val allVerbs = viewModel.allVerbs
    var currentVerb by remember { mutableStateOf(allVerbs.random()) }
    var options by remember { mutableStateOf(generateOptions(currentVerb, allVerbs)) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    fun checkAnswer() {
        if (selectedOption == currentVerb.past.form) {
            SoundPlayer.playCorrectSound()
            feedback = "Correct!"
            score++
        } else {
            SoundPlayer.playWrongSound()
            feedback = "Incorrect. The answer is ${currentVerb.past.form}."
        }
    }

    fun nextQuestion() {
        val newVerb = allVerbs.random()
        currentVerb = newVerb
        options = generateOptions(newVerb, allVerbs)
        selectedOption = null
        feedback = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        AppHeader()
        Text("Choose the Correct Past Form", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text("What is the past form of:", fontSize = 16.sp, color = Color.Gray)
        Text(
            text = currentVerb.infinitive.form,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { TTSPlayer.speak(currentVerb.infinitive.form) }
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

private fun generateOptions(correctVerb: Verb, allVerbs: List<Verb>): List<String> {
    val incorrectVerbs = allVerbs.filter { it != correctVerb }.shuffled().take(3)
    val options = incorrectVerbs.map { it.past.form }.toMutableList()
    options.add(correctVerb.past.form)
    return options.shuffled()
}