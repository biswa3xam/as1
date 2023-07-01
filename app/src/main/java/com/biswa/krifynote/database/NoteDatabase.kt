package com.biswa.krifynote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.biswa.krifynote.model.Note
import com.biswa.krifynote.utilities.DATABASE_NAME

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun getNoteDao() : NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        fun getDatabase(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext,NoteDatabase::class.java, DATABASE_NAME)
                            .build()
                }
            }
            return INSTANCE!!
        }
    }

}