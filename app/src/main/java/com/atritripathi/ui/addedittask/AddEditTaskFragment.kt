package com.atritripathi.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.atritripathi.tasks.R
import com.atritripathi.tasks.databinding.FragmentAddeditTaskBinding
import com.atritripathi.tasks.util.KEY_ADD_EDIT_REQUEST
import com.atritripathi.tasks.util.KEY_ADD_EDIT_RESULT
import com.atritripathi.tasks.util.exhaustive
import com.atritripathi.ui.addedittask.AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult
import com.atritripathi.ui.addedittask.AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

            etTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            cbImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskIsImportant = isChecked
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                    is NavigateBackWithResult -> {
                        binding.etTaskName.clearFocus()
                        setFragmentResult(
                            KEY_ADD_EDIT_REQUEST,
                            bundleOf(KEY_ADD_EDIT_RESULT to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive    // Ignore the warnings to keep exhaustive nature of when clause
            }
        }
    }
}