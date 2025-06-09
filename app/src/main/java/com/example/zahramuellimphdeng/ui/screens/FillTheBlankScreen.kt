package com.example.zahramuellimphdeng.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zahramuellimphdeng.ui.MainViewModel
// import com.example.zahramuellimphdeng.ui.common.AppHeader  // This import is no longer needed
import com.example.zahramuellimphdeng.utils.SoundPlayer
import com.example.zahramuellimphdeng.utils.TTSPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillTheBlankScreen(viewModel: MainViewModel) {
    val allVerbs = viewModel.allVerbs
    var currentVerb by remember { mutableStateOf(allVerbs.random()) }
    var pastInput by remember { mutableStateOf("") }
    var participleInput by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf<String?>(null) }
    var showCorrectAnswers by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    fun checkAnswers() {
        focusManager.clearFocus()
        val isPastCorrect = pastInput.trim().equals(currentVerb.past.form, ignoreCase = true)
        val isParticipleCorrect = participleInput.trim().equals(currentVerb.participle_ii.form, ignoreCase = true)

        if (isPastCorrect && isParticipleCorrect) {
            SoundPlayer.playCorrectSound()
            feedback = "Correct!"
            score++
            showCorrectAnswers = false

            coroutineScope.launch {
                delay(800)
                TTSPlayer.speak(currentVerb.infinitive.form)
                delay(600)
                TTSPlayer.speak(currentVerb.past.form)
                delay(600)
                TTSPlayer.speak(currentVerb.participle_ii.form)
            }

        } else {
            SoundPlayer.playWrongSound()
            feedback = "Incorrect. Try again or see the answer."
            showCorrectAnswers = true
        }
    }

    fun nextQuestion() {
        currentVerb = allVerbs.random()
        pastInput = ""
        participleInput = ""
        feedback = null
        showCorrectAnswers = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // AppHeader() has been REMOVED from here.

        Text("Fill in the Correct Forms", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Infinitive:", fontSize = 16.sp, color = Color.Gray)
        Text(currentVerb.infinitive.form, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = pastInput,
            onValueChange = { newText ->
                pastInput = newText
                SoundPlayer.playTypingSound()
            },
            label = { Text("Past Form") },
            singleLine = true,
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = participleInput,
            onValueChange = { newText ->
                participleInput = newText
                SoundPlayer.playTypingSound()
            },
            label = { Text("Participle II Form") },
            singleLine = true,
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { checkAnswers() })
        )

        Spacer(modifier = Modifier.height(24.dp))
        feedback?.let {
            val color = if (it == "Correct!") Color(0xFF4CAF50) else Color.Red
            Text(it, color = color, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
        if (showCorrectAnswers) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Correct answers: ${currentVerb.past.form}, ${currentVerb.participle_ii.form}")
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                SoundPlayer.playClickSound()
                checkAnswers()
            }) { Text("Check") }
            Button(onClick = {
                SoundPlayer.playClickSound()
                nextQuestion()
            }) { Text("Next Verb") }
        }
    }
}