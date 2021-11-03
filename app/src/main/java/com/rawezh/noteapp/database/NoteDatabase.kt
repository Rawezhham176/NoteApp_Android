package com.rawezh.noteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rawezh.noteapp.dao.NoteDao
import com.rawezh.noteapp.entities.Notes

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        var notesDatabase: NoteDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NoteDatabase {
            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                    context
                    , NoteDatabase::class.java
                    , "notes.db"
                ).build()
            }
            return notesDatabase!!
        }
    }

    abstract fun noteDao(): NoteDao
}