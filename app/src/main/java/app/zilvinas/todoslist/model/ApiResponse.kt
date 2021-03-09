package app.zilvinas.todoslist.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(

    @SerializedName("code")
    val code: Int,

    @SerializedName("data")
    val todoList: List<Todo>
)
