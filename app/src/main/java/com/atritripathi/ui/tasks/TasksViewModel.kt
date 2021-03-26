package com.atritripathi.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.atritripathi.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val _tasksFlow = searchQuery.flatMapLatest {
        taskDao.getTasks(it)
    }

    val tasks = _tasksFlow.asLiveData()
}