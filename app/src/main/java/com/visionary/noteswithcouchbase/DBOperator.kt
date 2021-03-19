package com.visionary.noteswithcouchbase

import android.content.Context
import com.couchbase.lite.*

class DBOperator private constructor(
    private val context: Context?,
    val dbManager: DBManager
) {

    companion object {

        fun getInstance(context: Context?): DBOperator =
            DBOperator(context, DBManager.getInstance(context))
    }

    private fun getDatabase(): Database? {
        return dbManager.getDatabase()
    }

    fun getAllNotes(): ArrayList<Note>? {
        val list = ArrayList<Note>()
        getDatabase()?.let {
            val query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(it))

            val resultSet = query.execute()
            var result = resultSet.next()
            while (result != null) {
                val valueMap = result.getDictionary(getDatabase()!!.name)
                val note = valueMap?.toMap()?.toDataClass<Note>()
                list.add(note!!)
                result = resultSet.next()
            }
        }
        return list
    }

    fun saveNote(note: Note) {
        val noteMap = note.serializeToMap()
        val document = MutableDocument(noteMap)
        document.setValue("id", document.id)
        try {
            getDatabase()?.save(document)
        } catch (e: CouchbaseLiteException) {
            e.printStackTrace()
        }
    }

    fun deleteNote(id: String) {
        dbManager.getDatabase().delete(dbManager.getDatabase().getDocument(id))
    }

    fun updateNote(id: String, newNote: Note) {
        deleteNote(id)
        saveNote(newNote)
    }

    fun getNote(id: String): Document {
        return dbManager.getDatabase().getDocument(id)
    }
}