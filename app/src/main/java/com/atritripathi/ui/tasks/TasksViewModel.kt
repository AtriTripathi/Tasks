package com.atritripathi.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.atritripathi.tasks.data.PreferencesManager
import com.atritripathi.tasks.data.SortOrder
import com.atritripathi.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferencesManager.preferencesFlow

    private val _tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, userPreferences ->
        Pair(query, userPreferences)
    }.flatMapLatest { (query, userPreferences) ->
        taskDao.getTasks(query, userPreferences.sortOrder, userPreferences.hideCompleted)
    }

    fun onSelectSortOrder(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onClickHideCompleted(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    val tasks = _tasksFlow.asLiveData()
}