package com.example.fullstackdevtest.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Variant(
    val id: Long,
    var productId: Long,
    val title: String? = null,
    val option2: String? = null,
    val price: Double? = null,
    val available: Boolean? = null
)