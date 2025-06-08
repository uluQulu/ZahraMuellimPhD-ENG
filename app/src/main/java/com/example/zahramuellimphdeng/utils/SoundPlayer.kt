package com.example.zahramuellimphdeng.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.zahramuellimphdeng.R

object SoundPlayer {

    // Players for sounds that are not rapid (played one at a time)
    private var correctSoundPlayer: MediaPlayer? = null
    private var wrongSoundPlayer: MediaPlayer? = null
    private var clickSoundPlayer: MediaPlayer? = null

    // We will NOT hold a permanent instance for the typing sound

    fun initialize(context: Context) {
        // Create the non-rapid players once
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
            player.setVolume(1.0f, 1.0f)
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun playWrongSound() {
        wrongSoundPlayer?.let { player ->
            player.setVolume(0.7f, 0.7f)
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun playClickSound() {
        clickSoundPlayer?.let { player ->
            player.setVolume(0.8f, 0.8f)
            if (player.isPlaying) {
                player.seekTo(0)
            }
            player.start()
        }
    }

    // *** THIS IS THE NEW, CORRECT LOGIC FOR THE TYPING SOUND ***
    fun playTypingSound(context: Context) {
        // Create a new, fresh player every single time the function is called.
        // This is the key to making it responsive.
        val typingSoundPlayer = MediaPlayer.create(context, R.raw.typing)

        typingSoundPlayer?.let { player ->
            player.setVolume(0.6f, 0.6f)
            // This is crucial: we add a listener that will automatically
            // release the resources as soon as the sound clip has finished playing.
            player.setOnCompletionListener { mp ->
                mp.release()
            }
            player.start()
        }
    }

    // Release the long-lived players when the app is destroyed
    fun release() {
        correctSoundPlayer?.release()
        correctSoundPlayer = null

        wrongSoundPlayer?.release()
        wrongSoundPlayer = null

        clickSoundPlayer?.release()
        clickSoundPlayer = null
    }
}