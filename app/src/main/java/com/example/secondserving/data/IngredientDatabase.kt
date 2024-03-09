package com.example.secondserving.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Ingredient::class], version = 1, exportSchema = false)
abstract class IngredientDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDAO

    //Dependency Injection means class that use other classes should not be responsible for creating or searching this using dagger, hilt uses dagger tool makes it easier

    class Callback @Inject constructor(
        private val database: Provider<IngredientDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() { //our own class that need instantiation and doesn't belong to third party library
        override fun onCreate(db: SupportSQLiteDatabase) { // first time when we create the database, called after build method
            super.onCreate(db)

            val dao = database.get().ingredientDao()

            applicationScope.launch {
                dao.insertIngredient(
                    Ingredient(
                        name = "Apple",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )
                dao.insertIngredient(
                    Ingredient(
                        name = "Carrot",
                        description = "Veggie",
                        unit = "Pieces"
                    )
                )
                dao.insertIngredient(
                    Ingredient(
                        name = "Orange",
                        description = "Fruit",
                        unit = "Pieces"
                    )
                )
                dao.insertIngredient(
                    Ingredient(
                        name = "Rice",
                        description = "Grain",
                        unit = "Kilograms"
                    )
                )
                dao.insertIngredient(
                    Ingredient(
                        name = "Wheat",
                        description = "Grain",
                        unit = "Kilogram"
                    )
                )
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }
}