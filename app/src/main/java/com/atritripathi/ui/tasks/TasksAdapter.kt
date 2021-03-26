package com.atritripathi.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atritripathi.tasks.data.Task
import com.atritripathi.tasks.databinding.ItemTasksBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder private constructor(
        private val binding: ItemTasksBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            with(binding) {
                cbCompleted.isChecked = task.isCompleted
                tvName.text = task.name
                tvName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant
            }
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val binding = ItemTasksBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return TaskViewHolder(binding)
            }
        }
    }

    private object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}