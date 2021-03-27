package com.atritripathi.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atritripathi.tasks.R
import com.atritripathi.tasks.databinding.FragmentAddeditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_addedit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddeditTaskBinding.bind(view)

        binding.apply {
            etTaskName.setText(viewModel.taskName)
            cbImportant.isChecked = viewModel.taskIsImportant
            cbImportant.jumpDrawablesToCurrentState()   // Avoid checkbox animation when starting.
            tvDateCreated.isVisible = viewModel.task != null    // Only show for existing task
            tvDateCreated.text = "Created: ${viewModel.task?.createdDateFormatted}"
        }
    }
}