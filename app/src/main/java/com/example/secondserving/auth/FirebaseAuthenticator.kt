package com.example.taskmanager.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticator : BaseAuthenticator {
    override suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser? {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        return FirebaseAuth.getInstance().currentUser
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {

        //TODO: check for duplicate emails
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return FirebaseAuth.getInstance().currentUser
    }

    override fun signOut(): FirebaseUser? {
        FirebaseAuth.getInstance().signOut()
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override suspend fun sendPasswordReset(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
    }

    override suspend fun deleteUser() {
        FirebaseAuth.getInstance().currentUser?.delete()?.await()
    }

    override suspend fun updateDisplayName(userName: String) {
        var userProfile = UserProfileChangeRequest.Builder().setDisplayName(userName).build()
        FirebaseAuth.getInstance().currentUser?.updateProfile(userProfile)
    }
}

