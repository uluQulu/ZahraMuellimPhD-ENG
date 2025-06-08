package com.example.zahramuellimphdeng.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.zahramuellimphdeng.R

// A singleton object to manage and play our custom sounds efficiently
object SoundPlayer {

    private var correctSoundPlayer: MediaPlayer? = null
    private var wrongSoundPlayer: MediaPlayer? = null
    private var clickSoundPlayer: MediaPlayer? = null

    // Initialize should be called once when the app starts
    fun initialize(context: Context) {
        // Create players only if they haven't been created yet
        if (correctSoundPlayer == null) {
            correctSoundPlayer = MediaPlayer.create(context, R.raw.correct)
        }
        if (wrongSoundPlayer == null) {
            wrongSoundPlayer = MediaPlayer.create(context, R.raw.wrong)
        }
        if (clickSoundPlayer == null) {
            clickSoundPlayer = MediaPlayer.create(context, R.raw.click)
        }
    }

    fun playCorrectSound() {
        correctSoundPlayer?.let { player ->
            // Set volume to maximum (left_channel, right_channel)
            player.setVolume(1.0f, 1.0f)
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun playWrongSound() {
        if (wrongSoundPlayer?.isPlaying == false) {
            wrongSoundPlayer?.start()
        }
    }

    fun playClickSound() {
        if (clickSoundPlayer?.isPlaying == false) {
            // Seek to the beginning if the sound is short and might be clicked rapidly
            clickSoundPlayer?.seekTo(0)
            clickSoundPlayer?.start()
        }
    }

    // Release resources when the app is destroyed to prevent memory leaks
    fun release() {
        correctSoundPlayer?.release()
        correctSoundPlayer = null

        wrongSoundPlayer?.release()
        wrongSoundPlayer = null

        clickSoundPlayer?.release()
        clickSoundPlayer = null
    }
}