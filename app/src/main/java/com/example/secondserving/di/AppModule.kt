package com.example.secondserving.di

import com.example.taskmanager.auth.AuthRepository
import com.example.taskmanager.auth.BaseAuthRepository
import com.example.taskmanager.auth.BaseAuthenticator
import com.example.taskmanager.auth.FirebaseAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module   //give dagger instructions on how to create the dependencies that we need
@InstallIn(SingletonComponent::class) //used throughout the app
object AppModule {

    @ApplicationScope  //not any coroutine scope, its an application scope, so dagger knows to differentiate between two coroutine scopes
    @Provides
    @Singleton //lives as long as app lives
    fun provideApplicationScope() = CoroutineScope(SupervisorJob()) // co routine gets cancelled when child fails


    //this means that anytime we need an authenticator Dagger will provide a Firebase authenticator.
    //in future if you want to swap out Firebase authentication for your own custom authenticator
    //you will simply come and swap here.
    @Singleton
    @Provides
    fun provideAuthenticator() : BaseAuthenticator =  FirebaseAuthenticator()


    //this just takes the same idea as the authenticator. If we create another repository class
    //we can simply just swap here
    @Singleton
    @Provides
    fun provideRepository(authenticator : BaseAuthenticator) : BaseAuthRepository = AuthRepository(authenticator)

}

@Retention(AnnotationRetention.RUNTIME) //qualifier will be visibile for reflection
@Qualifier //creating annotation
annotation class  ApplicationScope