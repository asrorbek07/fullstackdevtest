package com.example.fullstackdevtest.controller

import com.example.fullstackdevtest.domain.Product
import com.example.fullstackdevtest.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.math.absoluteValue
import kotlin.random.Random

@Controller
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/")
    fun findProducts(model: Model): String {
        return "product"
    }

    @GetMapping("/load_products")
    fun loadProducts(model: Model): String {
        model.addAttribute("products", productService.findAllProducts())
        return "product :: product-row"
    }

    @PostMapping("/register_product")
    fun registerProduct(
        model: Model,
        @RequestParam("title") title: String,
        @RequestParam("vendor") vendor: String,
        @RequestParam("productType") productType: String,
        @RequestParam("imageUrl", required = false) imageUrl: String?
    ): String {
        productService.registerProduct(
            Product(
                id = Random.nextLong().absoluteValue,
                title = title,
                vendor = vendor,
                productType = productType,
                imageUrl = imageUrl,
                variants = null
            )
        )
        model.addAttribute("products", productService.findAllProducts())
        return "product :: product-row"
    }
}