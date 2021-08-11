package com.example.todocompose.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocompose.todo.data.Todo
import com.example.todocompose.todo.data.TodoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
@Inject
constructor(private val todoRepo: TodoRepo) : ViewModel() {

    val response: MutableState<List<Todo>> = mutableStateOf(listOf())

    fun insert(todo: Todo) = viewModelScope.launch {
        todoRepo.insert(todo)
    }

    init {
        getAllTodos()
    }

    fun getAllTodos() = viewModelScope.launch {
        todoRepo.getAllTodos()
            .catch {
                    e ->
                Log.d("main","Exception: ${e.message}")
            }.collect {
                response.value = it
            }
    }
}