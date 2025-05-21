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
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

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

            // Logging user input
            Log.d("LoginInput", "Email: $email, Password: $password")

            // Make login request
            loginUser(email, password)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(email: String, password: String) {
        val client = OkHttpClient()

        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
            .url("http://10.0.2.2:3000/login") // 10.0.2.2 = localhost on Android emulator
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("LoginError", "Network Failure: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val message = jsonResponse.getString("message")
                            val user = jsonResponse.getJSONObject("user")
                            val role = user.getString("role")
                            val name = user.getString("name")

                            Toast.makeText(this@MainActivity, "$message\nWelcome $name", Toast.LENGTH_LONG).show()
                            Log.d("LoginSuccess", "User: $user")

                            // Navigate based on role
                            val intent = when (role.lowercase()) {
                                "officer" -> Intent(this@MainActivity, Officer_dashboard::class.java)
                                "principal" -> Intent(this@MainActivity, Principal_Dashboard::class.java)
                                "teacher" -> Intent(this@MainActivity, Teacher_Dashboard::class.java)
                                "warden" -> Intent(this@MainActivity, Warden_Dashboard::class.java)
                                else -> {
                                    Toast.makeText(this@MainActivity, "Unknown role: $role", Toast.LENGTH_SHORT).show()
                                    Log.e("LoginRole", "Unknown role received: $role")
                                    return@runOnUiThread
                                }
                            }
                            startActivity(intent)

                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "Invalid response format", Toast.LENGTH_SHORT).show()
                            Log.e("LoginParseError", "Error parsing: ${e.message}")
                        }
                    } else {
                        try {
                            val errorJson = JSONObject(responseBody ?: "{}")
                            val errorMessage = errorJson.optString("message", "Login failed")
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            Log.e("LoginFailed", errorMessage)
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "Login failed", Toast.LENGTH_SHORT).show()
                            Log.e("LoginError", "Failed response with unknown error.")
                        }
                    }
                }
            }

        })
    }
}
