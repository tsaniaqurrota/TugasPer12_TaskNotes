package com.example.tasknotes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task_table")

data class Task (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "description")
    val description : String,

    @ColumnInfo(name = "dosen")
    val dosen : String,

    @ColumnInfo(name = "deadline")
    val deadline : String
) : Serializable



