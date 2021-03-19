package com.visionary.noteswithcouchbase

import android.content.Context
import android.os.Environment
import com.couchbase.lite.*

class DBManager private constructor(private val context: Context?) {

    @Volatile
    private var database: Database? = null

    companion object {
        @Volatile
        private var INSTANCE: DBManager? = null

        fun getInstance(context: Context?): DBManager {
            CouchbaseLite.init(context!!)
            return INSTANCE ?: DBManager(context).also {
                INSTANCE = it
            }
        }
    }

    fun getDatabase(): Database {
        return database ?: createDatabase(context, DB_NAME).also {
            database = it
        }
    }

    private fun createDatabase(
        context: Context?,
        databaseName: String
    ): Database {
        val configuration = DatabaseConfiguration()
        configuration.directory = Environment.getExternalStorageDirectory().absolutePath
        return Database(databaseName, configuration)
    }

    fun closeDatabaseForUser() {
        try {
            database?.let {
                database?.close()
                database = null
            }
        } catch (e: CouchbaseLiteException) {
            e.printStackTrace()
        }
    }
}