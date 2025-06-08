package com.example.zahramuellimphdeng.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zahramuellimphdeng.R
import com.example.zahramuellimphdeng.data.Verb
import com.example.zahramuellimphdeng.ui.MainViewModel
import com.example.zahramuellimphdeng.utils.rememberSoundPlayers

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatchingGameScreen(viewModel: MainViewModel) {
    val allVerbs = viewModel.allVerbs
    var correctVerb by remember { mutableStateOf(allVerbs.random()) }
    var options by remember { mutableStateOf(generateMatchingOptions(correctVerb, allVerbs)) }
    var selectedWords by remember { mutableStateOf<List<String>>(emptyList()) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val soundPlayer = rememberSoundPlayers()

    fun checkAnswer() {
        val correctOrder = listOf(correctVerb.infinitive.form, correctVerb.past.form, correctVerb.participle_ii.form)
        if (selectedWords == correctOrder) {
            soundPlayer.correctPlayer.play()
            feedback = "Correct!"
            score++
        } else {
            soundPlayer.wrongPlayer.play()
            feedback = "Incorrect. The order is: ${correctOrder.joinToString(" -> ")}"
        }
    }

    fun nextQuestion() {
        val newVerb = allVerbs.random()
        correctVerb = newVerb
        options = generateMatchingOptions(newVerb, allVerbs)
        selectedWords = emptyList()
        feedback = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.logo_placeholder),
            contentDescription = "App Logo",
            modifier = Modifier
                .height(60.dp)
                .padding(bottom = 16.dp)
        )

        Text("Match the Forms in Order", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("(Infinitive -> Past -> Participle)", fontSize = 16.sp)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Your selection:", fontSize = 16.sp, color = Color.Gray)
        Text(
            text = selectedWords.joinToString(" -> "),
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
                        soundPlayer.clickPlayer.play()
                        if (selectedWords.size < 3 && !selectedWords.contains(option)) {
                            selectedWords = selectedWords + option
                        }
                    },
                    enabled = !selectedWords.contains(option)
                ) {
                    Text(option)
                }
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
                soundPlayer.clickPlayer.play()
                selectedWords = emptyList()
            }) { Text("Clear") }
            Button(
                onClick = {
                    soundPlayer.clickPlayer.play()
                    checkAnswer()
                },
                enabled = selectedWords.size == 3 && feedback == null
            ) { Text("Check") }
            Button(onClick = {
                soundPlayer.clickPlayer.play()
                nextQuestion()
            }) { Text("Next") }
        }
    }
}

private fun generateMatchingOptions(correctVerb: Verb, allVerbs: List<Verb>): List<String> {
    val correctForms = listOf(correctVerb.infinitive.form, correctVerb.past.form, correctVerb.participle_ii.form)
    val distractors = allVerbs.filter { it != correctVerb }.shuffled().take(2).map { it.infinitive.form }
    return (correctForms + distractors).shuffled()
}