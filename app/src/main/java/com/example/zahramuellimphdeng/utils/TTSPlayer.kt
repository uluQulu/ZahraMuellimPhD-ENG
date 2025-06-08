package com.example.zahramuellimphdeng.utils

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import java.util.UUID

object TTSPlayer {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return

        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                // *** THE FIX IS HERE: Corrected the typo ***
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

    fun speak(text: String) {
        if (!isInitialized) return

        val params = Bundle()
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)

        val utteranceId = UUID.randomUUID().toString()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        isInitialized = false
        tts = null
    }
}