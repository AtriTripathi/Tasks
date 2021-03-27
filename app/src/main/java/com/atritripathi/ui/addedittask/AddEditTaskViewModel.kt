package com.atritripathi.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atritripathi.tasks.data.Task
import com.atritripathi.tasks.data.TaskDao
import com.atritripathi.tasks.util.ADD_TASK_RESULT_OK
import com.atritripathi.tasks.util.EDIT_TASK_RESULT_OK
import com.atritripathi.ui.addedittask.AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClicked() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, isImportant = taskIsImportant)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, isImportant = taskIsImportant)
            createTask(newTask)
        }
    }

    private fun createTask(newTask: Task) = viewModelScope.launch {
        taskDao.insert(newTask)
        addEditTaskEventChannel.send(NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(updatedTask: Task) = viewModelScope.launch {
        taskDao.update(updatedTask)
        addEditTaskEventChannel.send(NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val message: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}