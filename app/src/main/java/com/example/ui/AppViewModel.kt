package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.CartItem
import com.example.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "pasu_ahaar_db"
    ).build()

    private val repository = AppRepository(db.productDao(), db.cartDao())

    val allProducts = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val cartItems = repository.allCartItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    data class CartItemWithProduct(
        val cartItem: CartItem,
        val product: Product
    )

    val cartWithProducts: StateFlow<List<CartItemWithProduct>> = combine(cartItems, allProducts) { items, products ->
        items.mapNotNull { item ->
            val product = products.find { it.id == item.productId }
            if (product != null) CartItemWithProduct(item, product) else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotal: StateFlow<Double> = cartWithProducts.map { list ->
        list.sumOf { it.product.price * it.cartItem.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Auth State
    private val _currentUser = MutableStateFlow<String?>(null) // null = logged out, "admin" = shop owner, "user" = customer
    val currentUser = _currentUser.asStateFlow()

    // Language State
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == "en") "hi" else "en"
    }

    // Error State
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    fun showError(msg: String) {
        _errorMessage.value = msg
    }

    init {
        // Pre-populate some dummy products if empty.
        viewModelScope.launch {
            repository.allProducts.collect { products ->
                if (products.isEmpty() && !_initialized) {
                    _initialized = true
                    prepopulateDb()
                }
            }
        }
    }
    private var _initialized = false

    private suspend fun prepopulateDb() {
        val feeds = listOf(
            Product(name = "Premium Cow Khal", category = "Feed", description = "High protein compressed animal feed for superior milk yield.", price = 1200.0, stockQuantity = 50),
            Product(name = "Nutri-mix Mineral Mixture", category = "Feed", description = "Essential vitamins and minerals specifically for cattle.", price = 450.0, stockQuantity = 30),
            Product(name = "Kapasya Mix", category = "Feed", description = "Blend of Kapasya to increase fat content in milk.", price = 1500.0, stockQuantity = 20),
            Product(name = "Goat Special Churi", category = "Feed", description = "Finely ground churi for easy digestion for goats.", price = 800.0, stockQuantity = 15),
            Product(name = "Liver Tonic 500ml", category = "Medicine", description = "General liver health and digestion enhancer.", price = 250.0, stockQuantity = 40),
            Product(name = "Deworming Liquid", category = "Medicine", description = "Broad spectrum dewormer for cattle and buffaloes.", price = 150.0, stockQuantity = 100),
            Product(name = "Calcium Gel 300g", category = "Medicine", description = "Instant calcium source post-calving.", price = 300.0, stockQuantity = 25),
            Product(name = "Super Bhoj - Milk Booster", category = "Appetite", description = "Special powder to boost appetite and milk production naturally.", price = 600.0, stockQuantity = 10),
            Product(name = "Digest-Pro Powder", category = "Appetite", description = "Helps in resolving digestive issues and boosting hunger.", price = 120.0, stockQuantity = 60)
        )
        feeds.forEach { repository.insertProduct(it) }
    }

    fun login(email: String, pass: String, isAdmin: Boolean) {
        if (isAdmin) {
             if ((email == "saqibjamal723@gmail.com" || email == "admin") && pass == "admin123") {
                 _currentUser.value = "admin"
             }
        } else {
             // For standard users, login allows any pass for now
             _currentUser.value = "user"
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existing = cartItems.value.find { it.productId == product.id }
            if (existing != null) {
                repository.updateCartQuantity(product.id, existing.quantity + 1)
            } else {
                repository.addToCart(product.id)
            }
        }
    }

    fun updateCartQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
             repository.updateCartQuantity(productId, quantity)
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Admin Operations
    fun addProduct(name: String, category: String, desc: String, price: Double, stock: Int) {
        viewModelScope.launch {
            repository.insertProduct(Product(name = name, category = category, description = desc, price = price, stockQuantity = stock))
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            repository.deleteProductById(id)
        }
    }
}
