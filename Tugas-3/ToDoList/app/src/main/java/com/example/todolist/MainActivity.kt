package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class TodoItem(
    val title: String,
    val completed: Boolean = false
)


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TodoApp()
                }
            }
        }
    }
}


@androidx.compose.runtime.Composable
fun TodoApp() {

    var taskText by remember {
        mutableStateOf("")
    }

    var tasks by remember {
        mutableStateOf(
            listOf(
                TodoItem("Learn Kotlin", true),
                TodoItem("Learn Compose"),
                TodoItem("Build Android App"),
                TodoItem("Submit Assignment")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "My To-Do List",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = taskText,
                onValueChange = {
                    taskText = it
                },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Enter a new task")
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.width(15.dp)
            )
            Button(
                onClick = {

                    if (taskText.isNotBlank()) {

                        tasks = tasks + TodoItem(
                            title = taskText.trim()
                        )

                        taskText = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            itemsIndexed(tasks) { index, task ->

                TodoRow(
                    task = task,

                    onCheckedChange = { checked ->

                        tasks = tasks.toMutableList().apply {
                            this[index] = task.copy(
                                completed = checked
                            )
                        }
                    },

                    onDelete = {

                        tasks = tasks.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                )
            }
        }
    }
}


@androidx.compose.runtime.Composable
fun TodoRow(
    task: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = task.completed,
            onCheckedChange = onCheckedChange
        )

        Text(
            text = task.title,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDelete
        ) {
            Text("🗑️")
        }
    }
}