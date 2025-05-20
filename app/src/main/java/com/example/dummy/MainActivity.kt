package com.example.dummy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.username_edit_text_login)
        val passwordEditText = findViewById<EditText>(R.id.password_edittext_login)
        val loginButton = findViewById<Button>(R.id.Login_button_login_page)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Print to Logcat
            Log.d("LoginData", "Email: $email, Password: $password")

            // Show in a Toast popup
            Toast.makeText(this, "Email: $email\nPassword: $password", Toast.LENGTH_LONG).show()
            // Navigate to OfficerDashboardActivity
            val intent = Intent(this, Officer_dashboard::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
