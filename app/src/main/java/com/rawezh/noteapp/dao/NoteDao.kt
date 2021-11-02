package com.rawezh.noteapp.dao

import androidx.room.*
import com.rawezh.noteapp.entities.Notes

@Dao
interface NoteDao {

    @Query("SELECT * FROM Notes ORDER BY id DESC")
    suspend fun getallNotes(): List<Notes>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: Notes)

    @Delete
    suspend fun deleteNotes(notes: Notes)
}