package com.atritripathi.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.atritripathi.tasks.R
import com.atritripathi.tasks.databinding.FragmentTasksBinding
import com.atritripathi.tasks.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTasksBinding.bind(view)
        val tasksAdapter = TasksAdapter()

        binding.rvTasks.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasksAdapter.submitList(tasks)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged { searchQuery ->
            viewModel.searchQuery.value = searchQuery
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }
            R.id.action_delete_completed_tasks -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}