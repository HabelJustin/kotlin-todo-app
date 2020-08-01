package com.example.todoappbystevdzasan.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todoappbystevdzasan.data.models.Todo
import com.example.todoappbystevdzasan.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase(application)
    private val repository: TodoRepository
    val sortByHighPriority: LiveData<List<Todo>>
    val sortByLowPriority: LiveData<List<Todo>>

    val getAllData: LiveData<List<Todo>>

    init {
        repository = TodoRepository(todoDao.todoDao())
        getAllData = repository.getAllData
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }

    fun deleteData(todoId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(todoId)
        }
    }

    fun updateData(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(todo)
        }
    }

    fun insertData(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(todo)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Todo>> {
        return repository.searchDatabase(searchQuery)
    }

}