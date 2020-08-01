package com.example.todoappbystevdzasan.data.repository

import androidx.lifecycle.LiveData
import com.example.todoappbystevdzasan.data.TodoDao
import com.example.todoappbystevdzasan.data.models.Todo

class TodoRepository(private val todoDao: TodoDao) {

    val getAllData: LiveData<List<Todo>> = todoDao.getAllData()
    val sortByHighPriority: LiveData<List<Todo>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<Todo>> = todoDao.sortByLowPriority()

    suspend fun updateData(todo: Todo) {
        todoDao.updateData(todo)
    }

    suspend fun insertData(todo: Todo) {
        todoDao.insertData(todo)
    }

    suspend fun deleteData(todoId: Int) {
        todoDao.deleteData(todoId)
    }

    suspend fun deleteAllData() {
        todoDao.deleteAllData()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Todo>> {
        return todoDao.searchDatabase(searchQuery)
    }
}