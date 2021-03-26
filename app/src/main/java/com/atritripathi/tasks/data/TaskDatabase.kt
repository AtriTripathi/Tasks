package com.atritripathi.tasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.atritripathi.tasks.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task(name = "Wash the dishes"))
                dao.insert(Task(name = "Do the laundry"))
                dao.insert(Task(name = "Buy groceries", isImportant = true))
                dao.insert(Task(name = "Prepare food", isCompleted = true))
                dao.insert(Task(name = "Call mom"))
                dao.insert(Task(name = "Repair my bike", isCompleted = true))
                dao.insert(Task(name = "Visit grandma"))
                dao.insert(Task(name = "Call Elon Musk"))
            }
        }
    }
}