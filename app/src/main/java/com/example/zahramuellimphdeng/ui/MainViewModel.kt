package com.example.zahramuellimphdeng.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.zahramuellimphdeng.data.Verb
import com.example.zahramuellimphdeng.data.VerbRepository

import com.example.zahramuellimphdeng.data.Noun
import com.example.zahramuellimphdeng.data.NounRepository


// We use AndroidViewModel to get access to the application context
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Create an instance of our repository
    private val repository = VerbRepository(application.applicationContext)
    // Expose the list of verbs to the UI
    val allVerbs: List<Verb> = repository.allVerbs


    private val nounRepository = NounRepository(application)
    val allNouns = nounRepository.allNouns

}