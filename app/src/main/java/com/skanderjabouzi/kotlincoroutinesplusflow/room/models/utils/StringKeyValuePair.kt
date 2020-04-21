package com.skanderjabouzi.kotlincoroutinesplusflow.room.models.utils

import androidx.room.Entity

@Entity(primaryKeys = ["key"])
data class StringKeyValuePair(
    val key: String, val value: String
)