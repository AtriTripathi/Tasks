package com.atritripathi.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.atritripathi.tasks.data.Task
import com.atritripathi.tasks.data.TaskDao
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_TASK = "task"
        const val KEY_TASK_NAME = "taskName"
        const val KEY_TASK_IS_IMPORTANT = "taskIsImportant"
    }

    val task = state.get<Task>(KEY_TASK)

    var taskName = state.get<String>(KEY_TASK_NAME) ?: task?.name ?: ""
        set(value) {
            field = value
            state.set(KEY_TASK_NAME, value)
        }

    var taskIsImportant = state.get<Boolean>(KEY_TASK_IS_IMPORTANT) ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set(KEY_TASK_IS_IMPORTANT, value)
        }
}