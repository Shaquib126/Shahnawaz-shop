package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppViewModel
import com.example.ui.LocalAppStrings

@Composable
fun AdminScreen(viewModel: AppViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }
    val products by viewModel.allProducts.collectAsStateWithLifecycle()
    val strings = LocalAppStrings.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text(strings.inventoryMgmt, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    ListItem(
                        headlineContent = { Text(product.name) },
                        supportingContent = { Text("${strings.stock}: ${product.stockQuantity} • ₹${product.price} • ${product.category}") },
                        trailingContent = {
                            IconButton(onClick = { viewModel.deleteProduct(product.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            viewModel = viewModel,
            onDismiss = { showAddDialog = false },
            onAdd = { name, category, desc, price, stock ->
                viewModel.addProduct(name, category, desc, price, stock)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddProductDialog(viewModel: AppViewModel, onDismiss: () -> Unit, onAdd: (String, String, String, Double, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Feed") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    val strings = LocalAppStrings.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addProduct) },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(strings.name) })
                Spacer(modifier = Modifier.height(8.dp))
                // Simple category dropdown logic simulated with buttons or just a field for now
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text(strings.categoryHint) })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text(strings.desc) })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text(strings.price) })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text(strings.stock) })
            }
        },
        confirmButton = {
            Button(onClick = {
                val p = price.toDoubleOrNull()
                val s = stock.toIntOrNull()
                if (name.isBlank() || p == null || s == null) {
                    viewModel.showError(strings.errorInvalidInput)
                } else {
                    onAdd(name, category, desc, p, s)
                }
            }) { Text(strings.addBtn) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}
