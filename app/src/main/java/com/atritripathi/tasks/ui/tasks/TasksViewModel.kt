package com.atritripathi.tasks.ui.tasks

import androidx.lifecycle.*
import com.atritripathi.tasks.data.PreferencesManager
import com.atritripathi.tasks.data.SortOrder
import com.atritripathi.tasks.data.Task
import com.atritripathi.tasks.data.TaskDao
import com.atritripathi.tasks.util.ADD_TASK_RESULT_OK
import com.atritripathi.tasks.util.EDIT_TASK_RESULT_OK
import com.atritripathi.tasks.ui.tasks.TasksViewModel.TasksEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val taskEventChannel = Channel<TasksEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()

    private val _tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, userPreferences ->
        Pair(query, userPreferences)
    }.flatMapLatest { (query, userPreferences) ->
        taskDao.getTasks(query, userPreferences.sortOrder, userPreferences.hideCompleted)
    }

    val tasks = _tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClicked(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        taskEventChannel.send(NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(isCompleted = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClicked(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClicked() = viewModelScope.launch {
        taskEventChannel.send(NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when(result) {
            ADD_TASK_RESULT_OK -> showTaskSavedMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedMessage("Task updated")
        }
    }

    private fun showTaskSavedMessage(text: String) = viewModelScope.launch {
        taskEventChannel.send(ShowTaskSavedMessage(text))
    }

    fun onDeleteAllCompletedClicked() = viewModelScope.launch {
        taskEventChannel.send(NavigateToDeleteAllCompletedScreen)
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedMessage(val message: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}