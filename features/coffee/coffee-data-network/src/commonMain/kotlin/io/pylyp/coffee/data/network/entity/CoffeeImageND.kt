package io.pylyp.coffee.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CoffeeImageND(
    @SerialName("file")
    val file: String,
)