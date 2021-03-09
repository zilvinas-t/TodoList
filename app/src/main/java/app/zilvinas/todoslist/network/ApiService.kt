package app.zilvinas.todoslist.network

import app.zilvinas.todoslist.model.ApiResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("public-api/todos")
    fun getTodoList(): Single<ApiResponse>
}