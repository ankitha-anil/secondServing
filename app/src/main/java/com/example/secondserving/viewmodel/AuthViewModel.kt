package com.example.secondserving.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.auth.BaseAuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.example.secondserving.auth.BaseAuthRepository
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: BaseAuthRepository,
) : ViewModel() {

    private val TAG = "AuthViewModel"


    //create the auth state livedata object that will be passed to
    //the home fragment and shall be used to control the ui i.e show authentication state
    //control behaviour of sign in and sign up button
    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser

    //create our channels that will be used to pass messages to the main ui
    //create event channel
    val eventsChannel = Channel<AllEvents>()

    //the messages passed to the channel shall be received as a Flowable
    //in the ui
    val allEventsFlow = eventsChannel.receiveAsFlow()


    //validate all fields first before performing any sign in operations
    fun signInUser(email: String, password: String) = viewModelScope.launch {
        when {
            email.isEmpty() -> {
                eventsChannel.send(AllEvents.ErrorCode(1))
            }

            password.isEmpty() -> {
                eventsChannel.send(AllEvents.ErrorCode(2))
            }

            else -> {
                actualSignInUser(email, password)
            }
        }
    }

    //validate all fields before performing any sign up operations
    fun signUpUser(email: String, password: String, confirmPass: String) = viewModelScope.launch {
        when {
            email.isEmpty() -> {
                eventsChannel.send(AllEvents.ErrorCode(1))
            }

            password.isEmpty() -> {
                eventsChannel.send(AllEvents.ErrorCode(2))
            }

            password != confirmPass -> {
                eventsChannel.send(AllEvents.ErrorCode(3))
            }

            else -> {
                actualSignUpUser(email, password)
            }
        }
    }


    private fun actualSignInUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user = repository.signInWithEmailPassword(email, password)
            user?.let {
                firebaseUser.postValue(it)
                eventsChannel.send(AllEvents.Message("Successful login!"))
            }
        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    private fun actualSignUpUser(email: String, password: String) = viewModelScope.launch {
        try {
            val user = repository.signUpWithEmailPassword(email, password)
            user?.let {
                firebaseUser.postValue(it)
                eventsChannel.send(AllEvents.Message("Successful Sign up"))
            } ?: eventsChannel.send(AllEvents.Error("Sign Up failed"))

        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signUpUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
        }
    }


    fun getCurrentUser() = viewModelScope.launch {
        val user = repository.getCurrentUser()
        firebaseUser.postValue(user)
    }

    fun changePassword(email: String) = viewModelScope.launch {
        if (email.isEmpty()) {
            eventsChannel.send(AllEvents.ErrorCode(1))
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eventsChannel.send(AllEvents.Error("Email does not exist"))
        }
        try {
            repository.sendResetPassword(email)
            eventsChannel.send(AllEvents.Message("Email sent to change password"))
        } catch (e: Exception) {
            val error = e.toString()
            Log.d(TAG, "passwordReset: ${error}")
            eventsChannel.send(AllEvents.Error(error))
        }
    }

    fun editUsername(userName: String) = viewModelScope.launch {

        if (userName.isEmpty()) {
            eventsChannel.send(AllEvents.Error("Username is empty"))
        }
        try {
            repository.updateDisplayName(userName)
            eventsChannel.send(AllEvents.StartMainActivity("Username updated successfully"))
        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "updateUsername: ${error[1]}")

            eventsChannel.send(AllEvents.Error(error[1]))

        }
    }

    fun deleteUserandSignOut(userPassword: String) = viewModelScope.launch {
        if (userPassword.isEmpty()) {
            eventsChannel.send(AllEvents.Error("Password is empty"))
        }
        try {
            val credential =
                EmailAuthProvider.getCredential(currentUser.value?.email.toString(), userPassword)
            currentUser.value?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                viewModelScope.launch {
                    if (reauthTask.isSuccessful) {
                        try {
                            repository.deleteUser()
                            val user = repository.signOut()

                            user?.let {
                                eventsChannel.send(AllEvents.Message("Deletion failure"))
                            }
                                ?: eventsChannel.send(AllEvents.StartMainActivity("Deleted account successfully"))
                        } catch (e: Exception) {
                            val error = e.toString().split(":").toTypedArray()
                            Log.d(TAG, "deleteUser: ${error[1]}")
                            eventsChannel.send(AllEvents.Error(error[1]))
                        }
                    } else {
                        eventsChannel.send(AllEvents.Message("Incorrect password"))

                    }
                }
            }
        } catch (e: Exception) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "deleteUser: ${error[1]}")

            eventsChannel.send(AllEvents.Error(error[1]))

        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            val user = repository.signOut()
            user?.let {
                eventsChannel.send(AllEvents.Message("Logout failure"))
            } ?: eventsChannel.send(AllEvents.StartMainActivity("Sign out successful"))
        } catch (e: FirebaseAuthException) {
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signOutUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    fun changePasswordAndSignOut() = viewModelScope.launch {
        try {
            repository.sendResetPassword(currentUser.value?.email.toString())
            val user = repository.signOut()
            user?.let {
                eventsChannel.send(AllEvents.Message("Logout failure"))
            }
                ?: eventsChannel.send(AllEvents.StartMainActivity("Email sent to change password. Login with new password"))
        } catch (e: Exception) {
            val error = e.toString()
            Log.d(TAG, "Password Reset and Logout: ${error}")
            eventsChannel.send(AllEvents.Error(error))
        }
    }


    sealed class AllEvents {
        data class Message(val message: String) : AllEvents()
        data class StartMainActivity(val message: String) : AllEvents()
        data class ErrorCode(val code: Int) : AllEvents()
        data class Error(val error: String) : AllEvents()
    }


}
