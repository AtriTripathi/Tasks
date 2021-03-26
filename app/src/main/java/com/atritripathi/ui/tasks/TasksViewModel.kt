package com.atritripathi.ui.tasks

import androidx.lifecycle.ViewModel
import com.atritripathi.tasks.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
}