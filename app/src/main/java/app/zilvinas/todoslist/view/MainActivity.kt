package app.zilvinas.todoslist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.zilvinas.todoslist.R
import app.zilvinas.todoslist.model.Todo
import app.zilvinas.todoslist.viewmodel.DatabaseViewModelFactory
import app.zilvinas.todoslist.viewmodel.TodoDatabaseViewModel
import app.zilvinas.todoslist.viewmodel.TodoNetworkViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var networkViewModel: TodoNetworkViewModel
    private lateinit var databaseViewModel: TodoDatabaseViewModel
    private lateinit var databaseViewModelFactory: DatabaseViewModelFactory
    private val todoListAdapter = TodoListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        networkViewModel = ViewModelProvider(this).get(TodoNetworkViewModel::class.java)
        databaseViewModelFactory = DatabaseViewModelFactory(application)
        databaseViewModel = ViewModelProvider(this, databaseViewModelFactory).get(TodoDatabaseViewModel::class.java)

        getDataFromDatabase()

        observeDatabaseViewModel()
        observeNetworkViewModel()
    }

    private fun initViews() {
        todo_list_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoListAdapter
        }

        todoListAdapter.onItemClick = { todo ->
            viewTodoDetails(todo)
        }

        swipe_refresh_layout.setOnRefreshListener {
            todo_list_recyclerView.visibility = View.GONE
            todo_list_loading.visibility = View.VISIBLE
            swipe_refresh_layout.isRefreshing = false
            getDataFromNetwork()
        }
    }

    private fun getDataFromDatabase() {
        GlobalScope.launch (Dispatchers.Main) { databaseViewModel.retrieveFromDatabase() }
    }

    private fun updateDatabase(newList: List<Todo>) {
        GlobalScope.launch (Dispatchers.Main) { databaseViewModel.saveToDatabase(newList) }
    }

    private fun getDataFromNetwork() {
        networkViewModel.fetchTodoList()
    }

    private fun observeNetworkViewModel() {
        todo_list_error.visibility = View.GONE
        networkViewModel.todoList.observe(this, { todoList ->
            todoList?.let {
                todo_list_recyclerView.visibility = View.VISIBLE
                todo_list_loading.visibility = View.GONE
                todoListAdapter.updateTodoList(todoList)
                updateDatabase(todoList)
            }
        })

        networkViewModel.isError.observe(this, { error ->
            error?.let {
                todo_list_loading.visibility = View.GONE
                if(it) {
                    todo_list_error.text = getString(R.string.todo_list_error)
                    getDataFromDatabase()
                } else {
                    todo_list_error.visibility = View.GONE
                }
            }
        })
    }

    private fun observeDatabaseViewModel() {
        todo_list_error.visibility = View.GONE
        databaseViewModel.todoList.observe(this, { todoList ->
            todoList?.let {
                if(it.isNotEmpty()) {
                    todo_list_recyclerView.visibility = View.VISIBLE
                    todo_list_loading.visibility = View.GONE
                    todoListAdapter.updateTodoList(todoList)
                } else {
                    getDataFromNetwork()
                }
            }
        })

        databaseViewModel.isError.observe(this, { error ->
            error?.let {
                todo_list_loading.visibility = View.GONE
                if(it) {
                    todo_list_error.text = getString(R.string.todo_list_error)
                    todo_list_recyclerView.visibility = View.GONE
                } else {
                    todo_list_error.visibility = View.GONE
                }
            }
        })
    }

    private fun viewTodoDetails(todo: Todo) {
        val encodedTodo = Gson().toJson(todo)
        val intent = Intent(this, TodoPreviewActivity::class.java).apply {
            putExtra(getString(R.string.todo_reference), encodedTodo)
        }
        startActivity(intent)
    }
}