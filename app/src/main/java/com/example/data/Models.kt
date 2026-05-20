package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // "Feed", "Medicine", "Appetite"
    val description: String,
    val price: Double,
    val stockQuantity: Int,
    val imageUrl: String = "https://images.unsplash.com/photo-1544414008-0112fd45df27?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3"
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val quantity: Int
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String, // Usually email or phone
    val name: String,
    val role: String // "Admin" or "User"
)
