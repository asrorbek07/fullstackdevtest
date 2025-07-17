package com.example.fullstackdevtest.domain

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Product(
    val id: Long,
    val title: String,
    val vendor: String? = null,
    val productType: String? = null,
    val imageUrl: String? = null,
    var variants: List<Variant>? = null
)