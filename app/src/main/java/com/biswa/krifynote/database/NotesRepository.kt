package com.biswa.krifynote.database

import androidx.lifecycle.LiveData
import com.biswa.krifynote.model.Note

class NotesRepository(private val noteDao: NoteDao) {

    fun allNotes() : LiveData<List<Note>>  = noteDao.getAllNote()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun delete(note : Note){
        noteDao.delete(note)
    }

    suspend fun update(note : Note){
        noteDao.update(id = note.id, title = note.title,note = note.note)
    }
}