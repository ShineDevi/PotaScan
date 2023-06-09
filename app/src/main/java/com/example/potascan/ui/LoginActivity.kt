package com.example.potascan.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.potascan.ViewModel.LoginViewModel
import com.example.potascan.data.Result
import com.example.potascan.databinding.ActivityLoginBinding
import com.example.potascan.ViewModel.ViewModelFactoryArticle

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactoryArticle.getInstance(dataStore)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

    }
    private fun setupAction() {
        binding.textView5.setOnClickListener{
            val intent =
                Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.evEmailLogin.text.toString()
            val password = binding.evPassLogin.text.toString()
            Log.d("isi email password: ", email+password)
            when {
                email.isEmpty() -> {
                    binding.evEmailLogin.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.evPassLogin.error = "Masukkan password"
                }
                else -> {
                    loginViewModel.login(email, password).observe(this) { loginResponse ->
                        Log.d("LoginTest", loginResponse.toString())
                        if (loginResponse != null) {
                            when (loginResponse) {
                                is Result.Loading -> {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Memproses login",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Result.Success -> {
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                }
                                is Result.Error -> {
                                    Toast.makeText(
                                        this,
                                        "Email atau password salah",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}