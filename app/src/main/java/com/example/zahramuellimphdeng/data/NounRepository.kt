package com.example.zahramuellimphdeng.data

import android.content.Context
import com.example.zahramuellimphdeng.R
import com.google.gson.Gson

class NounRepository(private val context: Context) {
    val allNouns: List<Noun> by lazy {
        loadNouns()
    }

    private fun loadNouns(): List<Noun> {
        val jsonString = context.resources.openRawResource(R.raw.nouns_linku)
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(jsonString, NounList::class.java).nouns
    }
}
