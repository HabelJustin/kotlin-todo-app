package com.example.todoappbystevdzasan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoappbystevdzasan.data.models.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: TodoDatabase? = null

        operator fun invoke(context: Context): TodoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

}