package com.example.zahramuellimphdeng.ui.screens.nouns

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.zahramuellimphdeng.data.Noun
import com.example.zahramuellimphdeng.ui.MainViewModel
import com.example.zahramuellimphdeng.utils.SoundPlayer
import com.example.zahramuellimphdeng.utils.TTSPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.FlowRow

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class
)
@Composable
fun FillTheGapNouns(viewModel: MainViewModel) {
    val allNouns = remember { viewModel.allNouns.filter { it.image_url != null }.shuffled() }
    var usedNounIndices by remember { mutableStateOf(mutableSetOf<Int>()) }
    var currentIndex by remember { mutableStateOf(0) }
    var currentNoun by remember { mutableStateOf(allNouns.getOrNull(currentIndex) ?: allNouns.first()) }
    var userInput by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf<String?>(null) }
    var showCorrectAnswer by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    // Set of dataset noun words lowercased
    val nounWordsSet = remember { allNouns.map { it.noun_en.lowercase() }.toSet() }

    fun maskSentence(noun: Noun): String {
        val target = noun.noun_en.lowercase()
        val sentence = noun.example_sentence
        val words = sentence.split(" ")

        // Replace *any* word starting with first 4 letters of target with continuous dashes
        val masked = words.map { word ->
            val cleaned = word.trimEnd('.', ',', '?', '!', ';', ':').lowercase()
            if (cleaned.startsWith(target.take(4))) {
                "_______" // continuous dashes without spaces
            } else word
        }.joinToString(" ")

        return masked
    }

    fun checkAnswer() {
        focusManager.clearFocus()
        if (userInput.trim().equals(currentNoun.noun_en, ignoreCase = true)) {
            SoundPlayer.playCorrectSound()
            feedback = "✅ Correct!"
            score++
            showCorrectAnswer = false

            coroutineScope.launch {
                delay(500)
                TTSPlayer.speak(currentNoun.noun_en)
            }
        } else {
            SoundPlayer.playWrongSound()
            feedback = "❌ Incorrect. Try again or see the answer."
            showCorrectAnswer = true
        }
    }

    fun nextQuestion() {
        usedNounIndices.add(currentIndex)

        if (usedNounIndices.size == allNouns.size) {
            usedNounIndices.clear()
        }

        val remainingIndices = allNouns.indices.filter { it !in usedNounIndices }
        currentIndex = if (remainingIndices.isNotEmpty()) {
            remainingIndices.random()
        } else 0

        currentNoun = allNouns[currentIndex]
        userInput = ""
        feedback = null
        showCorrectAnswer = false

        coroutineScope.launch {
            delay(300)
            TTSPlayer.speak(currentNoun.example_sentence)
        }
    }

    var popupWordIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fill in the Missing Noun", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Score: $score", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(currentNoun.image_url),
            contentDescription = "Noun image",
            modifier = Modifier
                .height(150.dp)
                .padding(8.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        val sentenceWords = currentNoun.example_sentence.split(" ")

        FlowRow(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sentenceWords.forEachIndexed { index, word ->
                val clean = word.trimEnd('.', ',', '?', '!', ';', ':')
                val isTargetWord = clean.lowercase().startsWith(currentNoun.noun_en.take(4).lowercase())
                val isInDataset = nounWordsSet.contains(clean.lowercase())
                val textColor = if (isInDataset && !isTargetWord) Color(0xFF3F51B5) else Color.Unspecified

                Box {
                    Text(
                        text = if (isTargetWord) "_______" else word,
                        fontSize = 20.sp,
                        color = textColor,
                        modifier = Modifier.clickable(enabled = !isTargetWord) {
                            // Speak all words except hidden dashed word
                            popupWordIndex = if (popupWordIndex == index) null else {
                                // For dataset words, show popup, else null popup
                                if (isInDataset && !isTargetWord) index else null
                            }
                            TTSPlayer.speak(clean)
                        }
                    )

                    // Show popup only on dataset words clicked and not the hidden word
                    if (popupWordIndex == index && isInDataset && !isTargetWord) {
                        val translation = allNouns.find {
                            it.noun_en.equals(clean, ignoreCase = true)
                        }?.translation_az ?: ""

                        if (translation.isNotEmpty()) {
                            Surface(
                                shadowElevation = 4.dp,
                                color = Color(0xCCFFFFFF),
                                tonalElevation = 6.dp,
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(top = 4.dp)
                            ) {
                                Text(
                                    text = translation,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentNoun.translation_az,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                SoundPlayer.playClickSound()
                TTSPlayer.speak(currentNoun.noun_en)
            }) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Play word", tint = Color(0xFF3F51B5))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val placeholderHint = when {
            currentNoun.noun_en.length <= 4 -> currentNoun.noun_en.take(1)
            currentNoun.noun_en.length == 5 -> currentNoun.noun_en.take(2)
            else -> currentNoun.noun_en.take(3)
        }

        OutlinedTextField(
            value = userInput,
            onValueChange = {
                userInput = it
                SoundPlayer.playTypingSound()
            },
            label = {
                Text("Enter the word (e.g. $placeholderHint...)")
            },
            singleLine = true,
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { checkAnswer() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        feedback?.let {
            val color = if (it.startsWith("✅")) Color(0xFF4CAF50) else Color.Red
            Text(text = it, color = color, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }

        if (showCorrectAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Correct answer: ${currentNoun.noun_en}",
                color = Color.DarkGray,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
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
                enabled = userInput.isNotBlank() && feedback == null
            ) {
                Text("Check")
            }

            Button(
                onClick = {
                    SoundPlayer.playClickSound()
                    nextQuestion()
                }
            ) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
