package com.example.todocompose.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.todocompose.todo.data.Todo
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoActivity : ComponentActivity() {
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Surface(color = MaterialTheme.colors.background) {
                AddToolbar()
            }

        }
    }

    @Composable
    fun AddToolbar() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "TodoCompose")
                    })
            },
            floatingActionButton = {
                val openDialog = remember {
                    mutableStateOf(false)
                }
                FloatingActionButton(onClick = { openDialog.value = true }) {
                    AddDialogBox(openDialog = openDialog)
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
        ) {

            RecyclerView(todoViewModel = todoViewModel)
            
        }


    }

    @Composable
    fun AddDialogBox(openDialog: MutableState<Boolean>) {
        val title = remember {
            mutableStateOf("")
        }
        val description = remember {
            mutableStateOf("")
        }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = { Text(text = "ToDo") },
                text = {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = {
                                title.value = it
                            },
                            placeholder = {
                                Text(text = "Enter Title")
                            },
                            label = {
                                Text(text = "Enter Title")
                            }, modifier = Modifier.padding(10.dp)
                        )
                        OutlinedTextField(
                            value = description.value,
                            onValueChange = {
                                description.value = it
                            },
                            placeholder = {
                                Text(text = "Enter Description")
                            },
                            label = {
                                Text(text = "Enter Description")
                            }, modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = {
                        insertToDo(title, description)
                        openDialog.value = false
                    }) {
                        Text(text = "Save")
                    }
                }
            )
        }//if
    }

    private fun insertToDo(title: MutableState<String>, description: MutableState<String>) {
        lifecycleScope.launchWhenStarted {

            if (!TextUtils.isEmpty(title.value) && !TextUtils.isEmpty(description.value)) {
                todoViewModel.insert(
                    Todo(
                        title.value,
                        description.value
                    )
                )

                Toast.makeText(this@TodoActivity, "inserted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@TodoActivity, "empty", Toast.LENGTH_LONG).show()

            }
        }

    }

    @Composable
    fun EachRow(todo:Todo) {

        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
                elevation = 2.dp,
                shape = RoundedCornerShape(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text= todo.title,fontWeight = FontWeight.ExtraBold)
                Text(text= todo.description,fontWeight = FontWeight.ExtraBold)
            }


        }
    }

    @Composable
    fun RecyclerView(todoViewModel: TodoViewModel){
        LazyColumn{
            items(todoViewModel.response.value){
                todo->
                EachRow(todo = todo )
            }
        }
    }



}

