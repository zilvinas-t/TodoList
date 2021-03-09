package app.zilvinas.todoslist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.zilvinas.todoslist.R
import app.zilvinas.todoslist.model.Todo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_todo_preview.*

class TodoPreviewActivity : AppCompatActivity() {

    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_preview)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.todo_preview_toolbar_title)

        if(getArticleDetails()) {
            setTodoDetails()
        }
    }

    private fun setTodoDetails() {
        todo_preview_title.text = todo.title
        todo_preview_date.text = todo.updatedAt.toString()
    }

    private fun getArticleDetails(): Boolean {
        try {
            val encodedArticle = intent.getStringExtra(getString(R.string.todo_reference))
            todo = Gson().fromJson(encodedArticle, Todo::class.java)
        } catch (e: Exception) {
            return false
        }
        return true
    }
}