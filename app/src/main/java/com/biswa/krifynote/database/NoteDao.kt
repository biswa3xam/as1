package com.biswa.krifynote.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.biswa.krifynote.model.Note

@Dao
interface NoteDao {

    @Query("Select * from notes_table order by id ASC")
    fun getAllNote(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)


    @Query("UPDATE notes_table SET title = :title, note = :note WHERE id = :id")
    suspend fun update(id: Int?, title: String?, note: String?)

}