package com.example.secondserving.viewmodel

import androidx.lifecycle.ViewModel
import com.example.secondserving.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

}