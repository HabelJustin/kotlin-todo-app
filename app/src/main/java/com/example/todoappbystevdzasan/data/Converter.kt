package com.example.todoappbystevdzasan.data

import androidx.room.TypeConverter
import com.example.todoappbystevdzasan.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

}