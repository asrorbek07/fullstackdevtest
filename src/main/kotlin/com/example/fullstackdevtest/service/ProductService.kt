package com.example.fullstackdevtest.service

import com.example.fullstackdevtest.domain.Product
import com.example.fullstackdevtest.dto.ProductResponse
import com.example.fullstackdevtest.repository.ProductRepository
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit

@Service
class ProductService(
    private val repo: ProductRepository,
) {
    private val url = "https://famme.no/products.json"

    @Scheduled(initialDelay = 0, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    fun fetchAndSaveProducts() {
        if (repo.retrieveProductCount() > 0) {
            return
        }
        val restTemplate = RestTemplate()
        val responseEntity = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            ProductResponse::class.java
        )
        val products = responseEntity.body?.products ?: emptyList<Product>()
        saveProducts(products)
    }

    fun getAllProducts(): List<Product> {
        return repo.retrieveAllProducts()
    }

    fun saveProduct(product: Product): Long {
        return repo.createProduct(product)
    }

    fun saveProducts(products: List<Product>) {
        products.take(10).forEach { product ->
            if (repo.existsProduct(product.id)) {
                return@forEach
            }
            val productId = saveProduct(product)
            product.variants?.forEach { variant ->
                variant.productId = productId
                repo.createVariant(variant)
            }
        }
    }
}