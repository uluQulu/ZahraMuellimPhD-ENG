package com.example.zahramuellimphdeng.data

// These are the same data classes we defined before
data class VerbList(val irregular_verbs: List<Verb>)
data class Verb(
    val infinitive: VerbForm,
    val past: VerbForm,
    val participle_ii: VerbForm,
    val translation: String
)
data class VerbForm(val form: String, val phonetic: String)