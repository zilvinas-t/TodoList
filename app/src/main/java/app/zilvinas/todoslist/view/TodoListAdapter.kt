package app.zilvinas.todoslist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.zilvinas.todoslist.R
import app.zilvinas.todoslist.model.Todo

class TodoListAdapter(
    private var todoList: ArrayList<Todo>,
    var onItemClick: ((Todo) -> Unit)? = null) : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    fun updateTodoList(items: List<Todo>) {
        todoList.clear()
        todoList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = todoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
    )

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.todo_title)

        fun bind(todo: Todo) {
            titleTextView.text = todo.title

            itemView.setOnClickListener {
                onItemClick?.invoke(todo)
            }
        }
    }
}