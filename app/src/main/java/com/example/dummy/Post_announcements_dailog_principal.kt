package com.example.dummy

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response

class Post_announcements_dailog_principal : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var postButton: Button

    // Assume this schoolId comes from the login response (pass it via intent)
    private var schoolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_post_announcements_dailog_principal)

        // Initialize views
        titleInput = findViewById(R.id.Title_enter_announcemnt_principle)
        messageInput = findViewById(R.id.announcement_message_input_principal)
        postButton = findViewById(R.id.Post_announcement_button_principal)

        // Get schoolId from intent
        schoolId = intent.getStringExtra("schoolId")
        Log.d("post announcement", "Received schoolId: $schoolId")

        postButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = messageInput.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Title and message can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                postAnnouncement(title, description)
            }
        }
    }

    private fun postAnnouncement(title: String, description: String) {
        val url = "https://school-management-app-five-drab.vercel.app/add-announcement"

        val queue = Volley.newRequestQueue(this)
        val jsonBody = JSONObject()
        jsonBody.put("schoolId", schoolId)
        jsonBody.put("title", title)
        jsonBody.put("description", description)

        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, "Announcement posted successfully", Toast.LENGTH_SHORT).show()
                finish()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Failed to post: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBodyContentType(): String = "application/json"

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray()
            }
        }

        queue.add(request)
    }
}
