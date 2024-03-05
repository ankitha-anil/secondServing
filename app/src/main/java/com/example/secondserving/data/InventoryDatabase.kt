package com.example.secondserving.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Inventory::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDAO

    //Dependency Injection means class that use other classes should not be responsible for creating or searching this using dagger, hilt uses dagger tool makes it easier

    class Callback @Inject constructor(
        private val database: Provider<InventoryDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() { //our own class that need instantiation and doesn't belong to third party library
        override fun onCreate(db: SupportSQLiteDatabase) { // first time when we create the database, called after build method
            super.onCreate(db)

            val dao = database.get().inventoryDao()

            applicationScope.launch {
                dao.insertInventory(Inventory("Kitchen", userID = "fSiLGeQcGDdVKHvH49jkqsGYsMz2"))
                dao.insertInventory(Inventory("Pantry", userID = "fSiLGeQcGDdVKHvH49jkqsGYsMz2"))

                dao.insertInventory(Inventory("Kitchen", userID = "ckjNy4ul2qSWfjd35O4iMoj1z2e2"))
                dao.insertInventory(Inventory("Pantry", userID = "ckjNy4ul2qSWfjd35O4iMoj1z2e2"))

                dao.insertInventory(Inventory("Kitchen", userID = "R77sUcKskwVkJSEREiCKLXFVXKd2"))
                dao.insertInventory(Inventory("Pantry", userID = "R77sUcKskwVkJSEREiCKLXFVXKd2"))

                dao.insertInventory(Inventory("Kitchen", userID = "tdhEkwujSXXbRgw3jvlE2z0qFV12"))
                dao.insertInventory(Inventory("Pantry", userID = "tdhEkwujSXXbRgw3jvlE2z0qFV12"))

                dao.insertInventory(Inventory("Kitchen", userID = "wwDMf3Q52iURZ3Fw7S1QBtRVvIs2"))
                dao.insertInventory(Inventory("Pantry", userID = "wwDMf3Q52iURZ3Fw7S1QBtRVvIs2"))
            }

        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }
}
