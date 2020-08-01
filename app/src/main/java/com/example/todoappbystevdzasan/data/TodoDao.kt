package com.example.todoappbystevdzasan.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoappbystevdzasan.data.models.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(todo: Todo)

    @Update
    suspend fun updateData(todo: Todo)

    @Query("DELETE FROM todo_table WHERE id = :todoId")
    suspend fun deleteData(todoId: Int)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllData()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<Todo>>
}