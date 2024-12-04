package com.example.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDTO(

    @SerialName("id") var id: String? = null,
    @SerialName("name") var name: String? = null

)
