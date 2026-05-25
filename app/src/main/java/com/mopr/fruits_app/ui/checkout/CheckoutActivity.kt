package com.mopr.fruits_app.ui.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.remote.FirestoreManager
import com.mopr.fruits_app.data.model.CartItem
import com.mopr.fruits_app.data.model.Order
import com.mopr.fruits_app.data.model.Address
import com.mopr.fruits_app.data.model.Promotion
import com.mopr.fruits_app.ui.home.HomeActivity
import com.mopr.fruits_app.util.BaseActivity
import kotlinx.coroutines.launch

class CheckoutActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var cartItems: List<CartItem> = emptyList()
    private var userId: String? = null
    
    private var selectedAddress: Address? = null
    private var selectedPaymentMethod: String = ""
    private var appliedPromotion: Promotion? = null
    private var originalTotal: Double = 0.0

    private val paymentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedPaymentMethod = result.data?.getStringExtra("SELECTED_PAYMENT") ?: ""
            findViewById<TextView>(R.id.tvSelectedPayment).text = if (selectedPaymentMethod.isNotEmpty()) selectedPaymentMethod else getString(R.string.no_payment_method_selected)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        firestoreManager = FirestoreManager()
        userId = firestoreManager.getCurrentUserId()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        findViewById<Button>(R.id.btnSelectAddress).setOnClickListener {
            showAddressSelectionDialog()
        }

        findViewById<Button>(R.id.btnSelectPayment).setOnClickListener {
            paymentLauncher.launch(Intent(this, PaymentActivity::class.java))
        }

        findViewById<Button>(R.id.btnApplyPromo).setOnClickListener {
            val code = findViewById<EditText>(R.id.editPromoCode).text.toString()
            if (code.isNotEmpty()) {
                applyPromoCode(code)
            }
        }

        loadCheckoutData()
        
        findViewById<Button>(R.id.btnPlaceOrder).setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            placeOrder()
        }
    }

    private fun loadCheckoutData() {
        val uid = userId ?: return
        lifecycleScope.launch {
            cartItems = firestoreManager.getCartItems(uid)
            if (cartItems.isEmpty()) {
                finish()
                return@launch
            }

            val summary = StringBuilder()
            originalTotal = 0.0
            for (item in cartItems) {
                summary.append("${item.fruitName} x${item.quantity} - $${String.format("%.2f", item.price * item.quantity)}\n")
                originalTotal += item.price * item.quantity
            }

            findViewById<TextView>(R.id.tvOrderSummary).text = summary.toString()
            updateTotalDisplay()
            
            // Auto-select default address if exists
            val addresses = firestoreManager.getAddresses(uid)
            selectedAddress = addresses.find { it.isDefault } ?: addresses.firstOrNull()
            updateAddressDisplay()
        }
    }

    private fun showAddressSelectionDialog() {
        val uid = userId ?: return
        lifecycleScope.launch {
            val addresses = firestoreManager.getAddresses(uid)
            if (addresses.isEmpty()) {
                Toast.makeText(this@CheckoutActivity, "No addresses found. Add one in your profile.", Toast.LENGTH_LONG).show()
                return@launch
            }

            val dialogView = LayoutInflater.from(this@CheckoutActivity).inflate(R.layout.dialog_select_address, null)
            val rv = dialogView.findViewById<RecyclerView>(R.id.rvDialogAddresses)
            rv.layoutManager = LinearLayoutManager(this@CheckoutActivity)
            
            val dialog = AlertDialog.Builder(this@CheckoutActivity)
                .setView(dialogView)
                .create()

            val adapter = AddressAdapter(addresses) { address ->
                selectedAddress = address
                updateAddressDisplay()
                dialog.dismiss()
            }
            rv.adapter = adapter
            dialog.show()
        }
    }

    private fun updateAddressDisplay() {
        findViewById<TextView>(R.id.tvSelectedAddress).text = selectedAddress?.let { "${it.name}: ${it.street}, ${it.city}" } ?: getString(R.string.no_address_selected)
    }

    private fun applyPromoCode(code: String) {
        lifecycleScope.launch {
            val promo = firestoreManager.validatePromoCode(code)
            val tvStatus = findViewById<TextView>(R.id.tvPromoStatus)
            tvStatus.visibility = View.VISIBLE
            if (promo != null) {
                appliedPromotion = promo
                tvStatus.text = "Promo applied: ${promo.discountPercent}% off!"
                tvStatus.setTextColor(getColor(R.color.leaf_green_top))
                updateTotalDisplay()
            } else {
                appliedPromotion = null
                tvStatus.text = "Invalid or inactive promo code"
                tvStatus.setTextColor(getColor(R.color.pink_favorite))
                updateTotalDisplay()
            }
        }
    }

    private fun updateTotalDisplay() {
        val tvTotal = findViewById<TextView>(R.id.tvTotalCheckout)
        var finalTotal = originalTotal
        if (appliedPromotion != null) {
            val discount = originalTotal * (appliedPromotion!!.discountPercent / 100.0)
            finalTotal -= discount
        }
        tvTotal.text = "Total: $${String.format("%.2f", finalTotal)}"
    }

    private fun placeOrder() {
        val uid = userId ?: return
        var finalTotal = originalTotal
        if (appliedPromotion != null) {
            val discount = originalTotal * (appliedPromotion!!.discountPercent / 100.0)
            finalTotal -= discount
        }

        val order = Order(
            userId = uid,
            items = cartItems,
            totalPrice = finalTotal,
            timestamp = System.currentTimeMillis(),
            address = selectedAddress,
            paymentMethod = selectedPaymentMethod
        )

        lifecycleScope.launch {
            if (firestoreManager.placeOrder(order)) {
                Toast.makeText(this@CheckoutActivity, "Order placed successfully!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@CheckoutActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this@CheckoutActivity, "Error placing order", Toast.LENGTH_SHORT).show()
            }
        }
    }
}