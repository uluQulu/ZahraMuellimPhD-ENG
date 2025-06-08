package com.example.zahramuellimphdeng.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.zahramuellimphdeng.R

object SoundPlayer {

    private var soundPool: SoundPool? = null
    private var correctSoundId: Int = 0
    private var wrongSoundId: Int = 0
    private var clickSoundId: Int = 0
    private var typingSoundId: Int = 0

    fun initialize(context: Context) {
        if (soundPool != null) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        correctSoundId = soundPool?.load(context, R.raw.correct, 1) ?: 0
        wrongSoundId = soundPool?.load(context, R.raw.wrong, 1) ?: 0
        clickSoundId = soundPool?.load(context, R.raw.click, 1) ?: 0
        typingSoundId = soundPool?.load(context, R.raw.typing, 1) ?: 0
    }

    // Parameters: (soundID, leftVolume, rightVolume, priority, loop, rate)

    fun playCorrectSound() {
        // HIGHEST PRIORITY (2)
        if (correctSoundId != 0) soundPool?.play(correctSoundId, 1.0f, 1.0f, 2, 0, 1.0f)
    }

    fun playWrongSound() {
        // HIGH PRIORITY (2)
        if (wrongSoundId != 0) soundPool?.play(wrongSoundId, 0.7f, 0.7f, 2, 0, 1.0f)
    }

    fun playClickSound() {
        // NORMAL PRIORITY (1)
        if (clickSoundId != 0) soundPool?.play(clickSoundId, 0.8f, 0.8f, 1, 0, 1.0f)
    }

    fun playTypingSound() {
        // LOWEST PRIORITY (0)
        if (typingSoundId != 0) soundPool?.play(typingSoundId, 0.6f, 0.6f, 0, 0, 1.0f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}