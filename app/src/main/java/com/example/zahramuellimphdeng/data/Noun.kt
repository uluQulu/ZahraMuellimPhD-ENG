package com.example.zahramuellimphdeng.data

data class NounList(val nouns: List<Noun>)

data class Noun(
    val noun_en: String,
    val level: String,
    val article: String?, // can be null
    val phonetic_us: String,
    val translation_az: String,
    val antonyms: List<String>,
    val synonyms: List<String>,
    val example_sentence: String,
    val example_phrase: String,
    val image_url: String
)
