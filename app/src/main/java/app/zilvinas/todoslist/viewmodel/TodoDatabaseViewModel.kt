package app.zilvinas.todoslist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.zilvinas.todoslist.database.AppDatabase
import app.zilvinas.todoslist.database.TodoDao
import app.zilvinas.todoslist.model.Todo
import java.lang.Exception

class TodoDatabaseViewModel(application: Application): AndroidViewModel(application){

    private val todoDao: TodoDao = AppDatabase.getDatabase(application).todoDao()

    val todoList = MutableLiveData<List<Todo>>()
    val isError = MutableLiveData<Boolean>()

    suspend fun saveToDatabase(newList: List<Todo>) {
        todoList.value = newList
        try {
            todoDao.insertAll(todoList = todoList.value!!)
            isError.value = false
        } catch (e: Exception) {
            isError.value = true
        }
    }

    suspend fun retrieveFromDatabase() {
        try {
            todoList.value = todoDao.getAll()
            isError.value = false
        } catch (e: Exception) {
            isError.value = true
        }
    }
}