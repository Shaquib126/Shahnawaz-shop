package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val allCartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()

    fun getProductsByCategory(category: String) = productDao.getProductsByCategory(category)
    
    suspend fun getProductById(id: Int) = productDao.getProductById(id)

    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)
    
    suspend fun deleteProductById(id: Int) = productDao.deleteProductById(id)

    suspend fun addToCart(productId: Int) {
        // Simple logic for adding to cart
        cartDao.insertCartItem(CartItem(productId = productId, quantity = 1))
    }

    suspend fun updateCartQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteByProductId(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    suspend fun clearCart() = cartDao.clearCart()
}
