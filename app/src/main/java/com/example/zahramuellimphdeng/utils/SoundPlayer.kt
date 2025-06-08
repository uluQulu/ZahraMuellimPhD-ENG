package com.example.zahramuellimphdeng.utils

import android.content.Context
import com.example.zahramuellimphdeng.R
import com.jarig.soloud.Soloud

// A singleton object to manage our sound engine
object SoundPlayer {
    private var soloud: Soloud? = null
    private var correctSoundId: Int = 0
    private var wrongSoundId: Int = 0
    private var clickSoundId: Int = 0

    // Initialize the sound engine and load sounds
    fun initialize(context: Context) {
        if (soloud == null) {
            soloud = Soloud()
            soloud?.init()

            // Load sounds from raw resources
            correctSoundId = soloud?.load(context, R.raw.correct) ?: 0
            wrongSoundId = soloud?.load(context, R.raw.wrong) ?: 0
            clickSoundId = soloud?.load(context, R.raw.click) ?: 0
        }
    }

    fun playCorrectSound() {
        if (correctSoundId != 0) soloud?.play(correctSoundId)
    }

    fun playWrongSound() {
        if (wrongSoundId != 0) soloud?.play(wrongSoundId)
    }

    fun playClickSound() {
        if (clickSoundId != 0) soloud?.play(clickSoundId)
    }

    // Clean up the engine when the app is destroyed
    fun release() {
        soloud?.deinit()
        soloud = null
    }
}