package com.example.zahramuellimphdeng.data

import android.content.Context
import com.example.zahramuellimphdeng.R
import com.google.gson.Gson

class VerbRepository(private val context: Context) {
    // A property that will hold our list of verbs after loading
    val allVerbs: List<Verb> by lazy {
        loadVerbs()
    }

    // A private function to read and parse the JSON from the raw resources
    private fun loadVerbs(): List<Verb> {
        val jsonString = context.resources.openRawResource(R.raw.verbs)
            .bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(jsonString, VerbList::class.java).irregular_verbs
    }
}