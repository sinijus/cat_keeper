package com.jaanussinivali.catkeeper.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jaanussinivali.catkeeper.R
import com.jaanussinivali.catkeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}