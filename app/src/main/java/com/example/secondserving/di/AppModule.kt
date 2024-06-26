package com.example.secondserving.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.secondserving.data.InventoryDatabase
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.auth.BaseAuthRepository
import com.example.secondserving.auth.BaseAuthenticator
import com.example.secondserving.auth.FirebaseAuthenticator
import com.example.secondserving.data.RecipeDAO
import com.example.secondserving.data.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

@Module   //give dagger instructions on how to create the dependencies that we need
@InstallIn(SingletonComponent::class) //used throughout the app
object AppModule {
    // ========================================= Inventory =========================================
    @Provides // we use provide method cause we dont own the classes
    @Singleton  //only one instance of task in whole app
    fun provideInventoryDatabase(
        app: Application, callback: InventoryDatabase.Callback
    ) = Room.databaseBuilder(app, InventoryDatabase::class.java, "inventory_database") //there is a circular dependency but oncreate is called after this
        .fallbackToDestructiveMigration()
        .addCallback(callback) //di code should not be responsible for db operations
        .build()

    @Provides
    fun provideInventoryDao(db: InventoryDatabase) = db.inventoryDao()


    // ========================================= Ingredient =========================================
    @Provides
    fun provideIngredientDao(db: InventoryDatabase) = db.ingredientDao()
    
    // ========================================= InventoryLineItem =========================================

    @Provides
    fun provideInventoryLineItemDao(db: InventoryDatabase) = db.inventoryLineItemDao()

    // ========================================= Recipe =========================================


    // Provides Application context
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context = application

//    @Provides
//    @Singleton
//    fun provideRecipeDatabase(
//        @ApplicationContext context: Context,
//        callback: RecipeDatabase.Callback
//    ): RecipeDatabase {
//        return Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
//            .fallbackToDestructiveMigration()
//            .addCallback(callback)
//            .build()
//    }

    @Provides
    fun provideRecipeDao(database: InventoryDatabase): RecipeDAO {
        return database.recipeDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        @ApplicationContext context: Context, // Provide the Application context
        database: Provider<InventoryDatabase>,
        @ApplicationScope coroutineScope: CoroutineScope
    ): InventoryDatabase.Callback {
        return InventoryDatabase.Callback(database, coroutineScope, context)
    }


    // ========================================= Basic App Functions =========================================

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

@Retention(AnnotationRetention.RUNTIME) //qualifier will be visible for reflection
@Qualifier //creating annotation
annotation class  ApplicationScope