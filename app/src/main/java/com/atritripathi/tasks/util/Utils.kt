package com.atritripathi.tasks.util

// Extension function to turn a statement into
// expression to have exhaustive when clause.
/**
 * Simple extension function to turn a when clause
 * exhaustive by converting all its statements into expressions.
 */
val <T> T.exhaustive: T
    get() = this