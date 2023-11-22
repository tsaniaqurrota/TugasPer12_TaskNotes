package com.example.tasknotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [Task::class],
    version = 1,
    exportSchema = false)
abstract class TaskRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao?

    companion object {
        @Volatile
        private var INSTANCE: TaskRoomDatabase? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context) : TaskRoomDatabase? {
            synchronized(TaskRoomDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TaskRoomDatabase::class.java, "task_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
