package com.example.dogapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.ActivityOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityOptionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.optionsHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

}