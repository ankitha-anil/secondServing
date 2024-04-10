package com.example.secondserving.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {


    abstract fun recipeDao(): RecipeDAO

    class Callback @Inject constructor(
        private val database: Provider<RecipeDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
        private val context: Context // This is correct now, no need to get context from database
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            applicationScope.launch {
                populateDatabaseFromCSV()
            }
        }

        private fun populateDatabaseFromCSV() {
            val dao = database.get().recipeDao()
            val inputStream = context.assets.open("RecipeCSV.csv") // Use the context directly
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            applicationScope.launch(Dispatchers.IO) {
                bufferedReader.use { reader ->
                    reader.readLine() // Skip CSV header
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val tokens = line!!.split(",")
                        if (tokens.size >= 7) {
                            val recipe = Recipe(
                                id = tokens[0].toInt(),
                                recipeID = tokens[1].toInt(),
                                recipeName = tokens[2],
                                created = tokens[3].toLong(),
                                recipeDescription = tokens[4],
                                recipeIngredients = tokens[5],
                                recipeSteps = tokens [6]
                            )
                            dao.insertRecipe(recipe)
                        }
                    }
                }
            }
        }
    }
}
