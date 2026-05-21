package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui.AppViewModel
import com.example.ui.LocalAppStrings

@Composable
fun LoginScreen(viewModel: AppViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdminLogin by remember { mutableStateOf(false) }
    val strings = LocalAppStrings.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            TextButton(onClick = { viewModel.toggleLanguage() }) {
                Text(strings.languageToggle)
            }
        }
        
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = strings.appTitle.split(" ").firstOrNull() ?: "",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = strings.appTitle.split(" ").drop(1).joinToString(" "),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(strings.emailOrPhone) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(strings.password) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAdminLogin, onCheckedChange = { isAdminLogin = it })
                    Text(strings.loginAsAdmin)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            viewModel.showError(strings.errorEmptyFields)
                        } else {
                            // Validation inside ViewModel, but we can do quick check if Admin
                            if (isAdminLogin && !((email == "saqibjamal723@gmail.com" || email == "admin") && password == "admin123")) {
                                viewModel.showError(strings.errorInvalidAdmin)
                            } else {
                                viewModel.login(email, password, isAdminLogin)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(strings.loginBtn)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(strings.loginHint, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
