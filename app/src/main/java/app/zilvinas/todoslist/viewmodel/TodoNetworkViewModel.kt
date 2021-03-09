package app.zilvinas.todoslist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.zilvinas.todoslist.model.ApiResponse
import app.zilvinas.todoslist.model.Todo
import app.zilvinas.todoslist.network.ApiFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class TodoNetworkViewModel: ViewModel() {

    private val apiFactory = ApiFactory()

    val todoList = MutableLiveData<List<Todo>>()
    val isError = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()

    fun fetchTodoList() {
        val apiService = apiFactory.provideApiService();
        disposable.add(apiService.getTodoList()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<ApiResponse>() {
                override fun onSuccess(response: ApiResponse) {
                    if(response.code == 200) {
                        isError.value = false
                        todoList.value = response.todoList
                    } else {
                        isError.value = true
                    }
                }

                override fun onError(e: Throwable) {
                    isError.value = true
                }
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}