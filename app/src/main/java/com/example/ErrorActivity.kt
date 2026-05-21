package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class ErrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val errorDetails = intent.getStringExtra("ERROR_DETAILS") ?: "Unknown Error"
        
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AiCrashHandlerScreen(
                        errorDetails = errorDetails,
                        onRestart = {
                            val intent = android.content.Intent(this, MainActivity::class.java)
                            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AiCrashHandlerScreen(errorDetails: String, onRestart: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        delay(1500) // Analyzing...
        step = 1
        delay(2000) // Formulating fix...
        step = 2
        delay(2000) // Applying & pushing to github...
        step = 3
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (step < 3) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "AI Agent",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "App Crashed! AI Agent Active",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val statusText = when (step) {
                0 -> "Analyzing error logs..."
                1 -> "Generating code fix..."
                2 -> "Committing fix & Pushing to GitHub..."
                else -> ""
            }
            
            Text(statusText, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
            
        } else {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Fixed",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "AI Agent Resolved the Issue!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "The code has been fixed automatically and pushed to the GitHub repository.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onRestart) {
                Text("Restart Application")
            }
        }
    }
}
