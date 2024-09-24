package com.example.chengkai_jingbo_jk_task_completion_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chengkai_jingbo_jk_task_completion_app.ui.theme.ChengkaiJingboJKTask_Completion_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChengkaiJingboJKTask_Completion_AppTheme {
                TaskCompletionTracker()
            }
        }
    }
}

data class Task(val name: String, val description: String, var isCompleted: Boolean = false, val emoji: String)

@Composable
fun TaskCompletionTracker() {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskList by remember { mutableStateOf(listOf<Task>()) }

    val anyCompletedTasks by remember { derivedStateOf { taskList.any { it.isCompleted } } }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Task Completion Tracker",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Task description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = {
                        if (taskName.isNotBlank() && taskDescription.isNotBlank()) {
                            taskList = taskList + Task(taskName, taskDescription, emoji = "❌")
                            taskName = ""
                            taskDescription = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add Task")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        taskList = taskList.filter { !it.isCompleted }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = anyCompletedTasks
                ) {
                    Text("Clear Completed")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                taskList.forEachIndexed { index, task ->
                    TaskBox(
                        task = task,
                        onTaskChecked = { isChecked ->
                            taskList = taskList.toMutableList().apply {
                                set(index, task.copy(isCompleted = isChecked))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskBox(task: Task, onTaskChecked: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onTaskChecked
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (!task.isCompleted) task.emoji else "✅",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (task.isCompleted) Color.Gray else Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (task.isCompleted) Color.Gray else Color.DarkGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskCompletionTracker() {
    ChengkaiJingboJKTask_Completion_AppTheme {
        TaskCompletionTracker()
    }
}
