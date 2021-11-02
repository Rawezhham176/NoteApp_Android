package com.rawezh.noteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rawezh.noteapp.dao.NoteDao
import com.rawezh.noteapp.entities.Notes
import java.security.AccessControlContext

@Database(entities = [Notes::class],version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        var noteDatabase: NoteDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NoteDatabase {
            if(noteDatabase != null){
                noteDatabase = Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "note.db"
                ).build()
            }
            return noteDatabase!!
        }

    }

    abstract fun noteDao():NoteDao
}