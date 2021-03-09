package app.zilvinas.todoslist.database

import androidx.annotation.WorkerThread
import androidx.room.*
import app.zilvinas.todoslist.model.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    @WorkerThread
    suspend fun getAll(): List<Todo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    suspend fun insertAll(todoList: List<Todo>)
}