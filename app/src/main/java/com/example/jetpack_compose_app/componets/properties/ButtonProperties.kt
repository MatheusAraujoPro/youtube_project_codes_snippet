package com.example.jetpack_compose_app.componets.properties

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.codandotv.craftd.androidcore.data.model.action.ActionProperties

@Immutable
@Stable
data class MyButtonProperties(
    @JsonProperty("text") val text: String? = null,
    @JsonProperty("align") val align: String? = null,
    @JsonProperty("color") val color: String? = null,
    @JsonProperty("actionProperties") val actionProperties: ActionProperties? = null,
)