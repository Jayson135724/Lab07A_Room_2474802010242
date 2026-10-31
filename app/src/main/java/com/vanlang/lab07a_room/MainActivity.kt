package com.vanlang.lab07a_room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vanlang.lab07a_room.data.local.AppDatabase
import com.vanlang.lab07a_room.data.local.ProductEntity
import com.vanlang.lab07a_room.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val productDao by lazy { AppDatabase.getInstance(this).productDao() }

    // Latest list emitted by the LiveData stream (newest first)
    private var products: List<ProductEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productDao.getAllProducts().observe(this) { list ->
            products = list
            binding.tvProducts.text = if (list.isEmpty()) {
                getString(R.string.empty_products)
            } else {
                list.joinToString("\n") {
                    getString(R.string.product_row, it.id, it.name, it.price, it.quantity)
                }
            }
        }

        binding.btnAdd.setOnClickListener { addProduct() }
        binding.btnDelete.setOnClickListener { deleteNewestProduct() }
    }

    private fun addProduct() {
        val name = binding.etName.text.toString().trim()
        val price = binding.etPrice.text.toString().toDoubleOrNull()
        val quantity = binding.etQuantity.text.toString().toIntOrNull()
        if (name.isEmpty() || price == null || quantity == null) {
            binding.etName.error = getString(R.string.invalid_input)
            return
        }

        lifecycleScope.launch {
            productDao.insertProduct(ProductEntity(name = name, price = price, quantity = quantity))
        }
        binding.etName.text?.clear()
        binding.etPrice.text?.clear()
        binding.etQuantity.text?.clear()
    }

    private fun deleteNewestProduct() {
        val newest = products.firstOrNull() ?: return
        lifecycleScope.launch {
            // Re-read by id to exercise getProductById() before deleting
            productDao.getProductById(newest.id)?.let { productDao.deleteProduct(it) }
        }
    }
}
