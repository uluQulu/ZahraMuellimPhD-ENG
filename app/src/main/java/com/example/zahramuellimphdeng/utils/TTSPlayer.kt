package com.example.zahramuellimphdeng.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TTSPlayer {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return

        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set the language to US English
                val result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The US English language specified is not supported!")
                } else {
                    isInitialized = true
                }
            } else {
                Log.e("TTS", "TTS Initialization Failed!")
            }
        }
    }

    // This is now the only function for speaking
    fun speak(text: String) {
        if (!isInitialized) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        isInitialized = false
        tts = null
    }
}