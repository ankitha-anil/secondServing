package com.example.secondserving.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondserving.MainActivity
import com.example.secondserving.auth.BaseAuthRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: BaseAuthRepository,
) : ViewModel() {

    private val TAG = "ProfileViewModel"
    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser

    private val eventsChannel = Channel<ProfileEvents>()
    val allEventsFlow = eventsChannel.receiveAsFlow()
    fun getCurrentUser() = viewModelScope.launch {
        val user = repository.getCurrentUser()
        firebaseUser.postValue(user)
    }

    fun signOut() = viewModelScope.launch {
        try {
            val user = repository.signOut()
            user?.let {
                eventsChannel.send(ProfileEvents.Message("Logout failure"))
            } ?: eventsChannel.send(ProfileEvents.Message("Sign out successful"))
        } catch (e: FirebaseAuthException) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signOutUser: ${error[1]}")
            eventsChannel.send(ProfileEvents.Error(error[1]))
        }
    }

    fun changePasswordAndSignOut() = viewModelScope.launch {
        try {
            repository.sendResetPassword(currentUser.value?.email.toString())
            val user = repository.signOut()
            user?.let {
                eventsChannel.send(ProfileEvents.Message("Logout failure"))
            }
                ?: eventsChannel.send(ProfileEvents.Message("Email sent to change password. Login with new password"))
        } catch (e: Exception) {
            val error = e.toString()
            Log.d(TAG, "passwordReset: ${error}")
            eventsChannel.send(ProfileEvents.Error(error))
        }

    }

    fun editEmail() = viewModelScope.launch {

    }

    fun editUsername() = viewModelScope.launch {

    }

    fun deleteUser() = viewModelScope.launch {

    }

    fun startMyActivity(activity: Activity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }

    sealed class ProfileEvents {
        data class Message(val message: String) : ProfileEvents()
        data class ErrorCode(val code: Int) : ProfileEvents()
        data class Error(val error: String) : ProfileEvents()
    }
}