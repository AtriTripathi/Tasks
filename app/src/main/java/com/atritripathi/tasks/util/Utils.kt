package com.atritripathi.tasks.util

import android.app.Activity

// Extension function to turn a statement into
// expression to have exhaustive when clause.
/**
 * Simple extension function to turn a when clause
 * exhaustive by converting all its statements into expressions.
 */
val <T> T.exhaustive: T
    get() = this

const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1

const val KEY_ADD_EDIT_REQUEST = "add_edit_request"
const val KEY_ADD_EDIT_RESULT = "add_edit_result"