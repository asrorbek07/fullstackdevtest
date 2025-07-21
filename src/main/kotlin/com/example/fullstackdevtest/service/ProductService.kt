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

    @Scheduled(initialDelay = 0, fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    fun fetchAndRegisterProducts() {
        if (repo.retrieveProductCount() >= 50) {
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
        registerProducts(products)
    }

    fun findAllProducts(): List<Product> {
        return repo.retrieveAllProducts()
    }

    fun registerProduct(product: Product): Long {
        return repo.createProduct(product)
    }

    fun registerProducts(products: List<Product>) {
        products
            .filterNot { repo.existsProduct(it.id) }
            .take(5)
            .forEach { product ->
                val productId = registerProduct(product)
                product.variants?.forEach { variant ->
                    variant.productId = productId
                    repo.createVariant(variant)
                }
            }
    }

    fun deleteProductById(productId: Long): Boolean {
        if (!repo.existsProduct(productId)) {
            throw IllegalArgumentException("Product with id $productId does not exist")
        }
        return repo.deleteProduct(productId)
    }

}