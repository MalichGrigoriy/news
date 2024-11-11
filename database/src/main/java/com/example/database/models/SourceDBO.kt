package com.example.database.models

import androidx.room.ColumnInfo

data class SourceDBO(
    @ColumnInfo("id") var id: String? = null,
    @ColumnInfo("name") var name: String? = null
)