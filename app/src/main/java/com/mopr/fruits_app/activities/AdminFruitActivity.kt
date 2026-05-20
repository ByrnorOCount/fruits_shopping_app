package com.mopr.fruits_app.activities

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.appbar.MaterialToolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.mopr.fruits_app.R
import com.mopr.fruits_app.database.FirestoreManager
import com.mopr.fruits_app.models.Fruit
import kotlinx.coroutines.launch
import java.util.UUID

class AdminFruitActivity : BaseActivity() {
    private lateinit var firestoreManager: FirestoreManager
    private var selectedImageRes: Int = R.drawable.ic_add
    private var editingFruit: Fruit? = null

    private lateinit var etName: TextInputEditText
    private lateinit var etPrice: TextInputEditText
    private lateinit var etScientific: TextInputEditText
    private lateinit var etDesc: TextInputEditText
    private lateinit var etBenefits: TextInputEditText
    private lateinit var ivSelected: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_fruit)

        firestoreManager = FirestoreManager()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        etName = findViewById(R.id.etFruitName)
        etPrice = findViewById(R.id.etFruitPrice)
        etScientific = findViewById(R.id.etScientificName)
        etDesc = findViewById(R.id.etDescription)
        etBenefits = findViewById(R.id.etHealthBenefits)
        ivSelected = findViewById(R.id.ivSelectedFruit)

        editingFruit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("FRUIT_OBJ", Fruit::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("FRUIT_OBJ")
        }

        editingFruit?.let { fruit ->
            etName.setText(fruit.name)
            etPrice.setText(fruit.price.toString())
            etScientific.setText(fruit.scientificName)
            etDesc.setText(fruit.description)
            etBenefits.setText(fruit.healthBenefits)
            selectedImageRes = fruit.imageRes
            ivSelected.setImageResource(selectedImageRes)
            supportActionBar?.title = getString(R.string.edit_fruit_title, fruit.name)
        }

        findViewById<View>(R.id.cardSelectImage).setOnClickListener {
            showImagePicker()
        }

        findViewById<Button>(R.id.btnSaveFruit).setOnClickListener {
            saveFruit()
        }
    }

    private fun showImagePicker() {
        val imageList = listOf(
            R.drawable.img_fruit_apple, R.drawable.img_fruit_banana, R.drawable.img_fruit_cherry,
            R.drawable.img_fruit_date, R.drawable.img_fruit_elderberry, R.drawable.img_fruit_fig,
            R.drawable.img_fruit_grape, R.drawable.img_fruit_honeydew, R.drawable.img_fruit_kiwi,
            R.drawable.img_fruit_lemon, R.drawable.img_fruit_mango, R.drawable.img_fruit_nectarine,
            R.drawable.img_fruit_orange, R.drawable.img_fruit_papaya, R.drawable.img_fruit_quince,
            R.drawable.img_fruit_raspberry, R.drawable.img_fruit_strawberry
        )

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_image_picker, null)
        val gridView = dialogView.findViewById<GridView>(R.id.gvImagePicker)
        
        val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle(R.string.select_image_title)
            .setView(dialogView)
            .create()

        gridView.adapter = object : BaseAdapter() {
            override fun getCount(): Int = imageList.size
            override fun getItem(position: Int): Any = imageList[position]
            override fun getItemId(position: Int): Long = position.toLong()
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: LayoutInflater.from(this@AdminFruitActivity)
                    .inflate(R.layout.item_image_picker, parent, false)
                val iv = view.findViewById<ImageView>(R.id.ivPickerImage)
                iv.setImageResource(imageList[position])
                iv.setOnClickListener {
                    selectedImageRes = imageList[position]
                    ivSelected.setImageResource(selectedImageRes)
                    dialog.dismiss()
                }
                return view
            }
        }

        dialog.show()
    }

    private fun saveFruit() {
        val name = etName.text.toString()
        val priceStr = etPrice.text.toString()
        val scientific = etScientific.text.toString()
        val desc = etDesc.text.toString()
        val benefits = etBenefits.text.toString()

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, R.string.error_required_fields, Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val id = editingFruit?.id ?: name.lowercase().replace(" ", "_") // Simple ID generation

        val fruit = Fruit(
            id = id,
            name = name,
            price = price,
            imageRes = selectedImageRes,
            description = desc,
            scientificName = scientific,
            healthBenefits = benefits
        )

        lifecycleScope.launch {
            val success = if (editingFruit != null) {
                firestoreManager.updateFruit(fruit)
            } else {
                firestoreManager.addFruit(fruit)
            }

            if (success) {
                Toast.makeText(this@AdminFruitActivity, R.string.success_fruit_saved, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@AdminFruitActivity, R.string.error_saving_fruit, Toast.LENGTH_SHORT).show()
            }
        }
    }
}