package com.example.secondserving.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.secondserving.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [InventoryLineItem::class], version = 1, exportSchema = false)
abstract class InventoryLineItemDatabase: RoomDatabase() {
    abstract fun inventoryLineItemDao(): InventoryLineItemDAO

    //Dependency Injection means class that use other classes should not be responsible for creating or searching this using dagger, hilt uses dagger tool makes it easier

    class Callback @Inject constructor(
        private val database: Provider<InventoryLineItemDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() { //our own class that need instantiation and doesn't belong to third party library
        override fun onCreate(db: SupportSQLiteDatabase) { // first time when we create the database, called after build method
            super.onCreate(db)

            val dao = database.get().inventoryLineItemDao()

            applicationScope.launch {
                dao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryId = 1,
                        ingredientId = 1,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 1,
                    )
                )
                dao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryId = 2,
                        ingredientId = 1,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 3,
                    )
                )
                dao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryId = 1,
                        ingredientId = 2,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 5,
                    )
                )
                dao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryId = 2,
                        ingredientId = 2,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 7,
                    )
                )
                dao.insertInventoryLineItem(
                    InventoryLineItem(
                        inventoryId = 1,
                        ingredientId = 3,
                        expiryDate = System.currentTimeMillis(),
                        quantity = 9,
                    )
                )
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }

}