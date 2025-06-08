package com.example.zahramuellimphdeng.ui.screens

import androidx.compose.foundation.layout.*
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
import java.util.UUID

// data class Feedback(val message: String, val isCorrect: Boolean, val id: String = UUID.randomUUID().toString())
// This class is still needed for the unique buttons
data class MatchOption(
    val id: String = UUID.randomUUID().toString(),
    val text: String
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchingGameScreen(viewModel: MainViewModel) {
    val allVerbs = viewModel.allVerbs
    var correctVerb by remember { mutableStateOf(allVerbs.random()) }
    var options by remember { mutableStateOf(generateMatchingOptions(correctVerb, allVerbs)) }
    var selectedOptions by remember { mutableStateOf<List<MatchOption>>(emptyList()) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    fun checkAnswer() {
        val selectedWords = selectedOptions.map { it.text }
        val correctOrder = listOf(correctVerb.infinitive.form, correctVerb.past.form, correctVerb.participle_ii.form)
        if (selectedWords == correctOrder) {
            SoundPlayer.playCorrectSound()
            feedback = "Correct!"
            score++
        } else {
            SoundPlayer.playWrongSound()
            feedback = "Incorrect. The order is: ${correctOrder.joinToString(" -> ")}"
        }
    }

    fun nextQuestion() {
        val newVerb = allVerbs.random()
        correctVerb = newVerb
        options = generateMatchingOptions(newVerb, allVerbs)
        selectedOptions = emptyList()
        feedback = null
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()
        Text("Match the Forms in Order", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("(Infinitive -> Past -> Participle)", fontSize = 16.sp)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Your selection:", fontSize = 16.sp, color = Color.Gray)
        Text(
            text = selectedOptions.joinToString(" -> ") { it.text },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.height(30.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = {
                        TTSPlayer.speak(option.text)
                        SoundPlayer.playClickSound()
                        if (selectedOptions.size < 3) {
                            selectedOptions = selectedOptions + option
                        }
                    },
                    enabled = selectedOptions.none { it.id == option.id } && feedback == null
                ) { Text(option.text) }
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
            Button(onClick = {
                SoundPlayer.playClickSound()
                selectedOptions = emptyList()
            }) { Text("Clear") }
            Button(
                onClick = {
                    SoundPlayer.playClickSound()
                    checkAnswer()
                },
                enabled = selectedOptions.size == 3 && feedback == null
            ) { Text("Check") }
            Button(onClick = {
                SoundPlayer.playClickSound()
                nextQuestion()
            }) { Text("Next") }
        }
    }
}
private fun generateMatchingOptions(correctVerb: Verb, allVerbs: List<Verb>): List<MatchOption> {
    val correctForms = listOf(
        MatchOption(text = correctVerb.infinitive.form),
        MatchOption(text = correctVerb.past.form),
        MatchOption(text = correctVerb.participle_ii.form)
    )
    val distractors = allVerbs.filter { it != correctVerb }
        .shuffled()
        .take(2)
        .map { MatchOption(text = it.infinitive.form) }

    return (correctForms + distractors).shuffled()
}