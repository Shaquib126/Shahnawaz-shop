package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.ui.AppViewModel
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentUser = viewModel.currentUser.collectAsState().value
                
                Box(modifier = Modifier.fillMaxSize()) {
                    if (currentUser == null) {
                        LoginScreen(viewModel)
                    } else {
                        MainScreen(
                            viewModel = viewModel,
                            onCheckout = {
                                val total = viewModel.cartTotal.value
                                if (total > 0) {
                                    launchUpiPayment(this@MainActivity, total)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun launchUpiPayment(context: Context, amount: Double) {
        val upiId = "9616169461@upi" // Dummy UPI ID from phone number
        val name = "Shahnawaz Pasu Ahaar Center"
        val note = "Order Payment"
        val amountStr = String.format("%.2f", amount)

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amountStr)
            .appendQueryParameter("cu", "INR")
            .build()
        
        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri
        
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        
        if (chooser.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
            // Note: After payment, we would ideally start a result launcher and clear cart.
            // For now, we simulate success message
            Toast.makeText(context, "UPI intent launched for Shahnawaz (+919616169461)", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "No UPI app found on device, clearing cart anyway", Toast.LENGTH_LONG).show()
            viewModel.clearCart()
        }
    }
}
