package com.example.fullstackdevtest.repository

import com.example.fullstackdevtest.domain.Product
import com.example.fullstackdevtest.domain.Variant
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jdbcClient: JdbcClient
) {
    private val GET_PRODUCT_COUNT_SQL = "SELECT COUNT(*) FROM products"
    private val GET_ALL_PRODUCTS_SQL = """
        SELECT id, title, vendor, product_type, image_url
        FROM products
    """
    private val GET_ALL_PRODUCT_VARIANTS_SQL = """
        SELECT id, product_id, title, option2, price, available
        FROM variants
        WHERE product_id = ?
    """
    private val SAVE_PRODUCT_SQL = """
        INSERT INTO products (id, title, vendor, product_type, image_url)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id
    """
    private val SAVE_VARIANT_SQL = """
        INSERT INTO variants (id, product_id, title, option2, price, available)
        VALUES (?, ?, ?, ?, ?, ?)
    """
    private val EXISTS_PRODUCT_SQL = """
        SELECT COUNT(*) FROM products WHERE id = ?
    """

    fun retrieveProductCount(): Long =
        jdbcClient.sql(GET_PRODUCT_COUNT_SQL)
            .query(Long::class.java)
            .single()

    fun retrieveAllProducts(): List<Product> {
        var products = jdbcClient.sql(GET_ALL_PRODUCTS_SQL)
            .query(productRowMapper)
            .list()

        return products.map { product ->
            var variants = retrieveAllProductVariants(product.id)
            product.apply { this.variants = variants }
        }
    }

    private fun retrieveAllProductVariants(productId: Long): List<Variant> =
        jdbcClient.sql(GET_ALL_PRODUCT_VARIANTS_SQL)
            .params(productId)
            .query(variantRowMapper)
            .list()

    fun createProduct(product: Product): Long =
        jdbcClient.sql(SAVE_PRODUCT_SQL)
            .params(product.id, product.title, product.vendor, product.productType, product.imageUrl)
            .query(Long::class.java)
            .single()

    fun createVariant(variant: Variant) {
        jdbcClient.sql(SAVE_VARIANT_SQL)
            .params(
                variant.id,
                variant.productId,
                variant.title,
                variant.option2,
                variant.price ?: 0.0,
                variant.available ?: false
            )
            .update()
    }

    fun existsProduct(productId: Long): Boolean =
        jdbcClient.sql(EXISTS_PRODUCT_SQL)
            .params(productId)
            .query(Long::class.java)
            .single() > 0

    private val productRowMapper = RowMapper { rs, _ ->
        Product(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            vendor = rs.getString("vendor"),
            productType = rs.getString("product_type"),
            imageUrl = rs.getString("image_url"),
            variants = null
        )
    }

    private val variantRowMapper = RowMapper { rs, _ ->
        Variant(
            id = rs.getLong("id"),
            productId = rs.getLong("product_id"),
            title = rs.getString("title"),
            option2 = rs.getString("option2"),
            price = rs.getDouble("price").takeIf { !rs.wasNull() },
            available = rs.getBoolean("available").takeIf { !rs.wasNull() }
        )
    }
}