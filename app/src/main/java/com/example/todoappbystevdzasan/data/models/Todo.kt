package com.example.todoappbystevdzasan.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String
) : Parcelable