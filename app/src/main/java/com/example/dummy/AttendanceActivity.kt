package com.example.dummy

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AttendanceActivity : AppCompatActivity() {

    private lateinit var adapter: StudentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var submitButton: Button
    private var schoolId: String = ""
    private var type: String = "student"  // either "student" or "cooking"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        schoolId = intent.getStringExtra("schoolId") ?: ""
        type = intent.getStringExtra("type") ?: "student"

        recyclerView = findViewById(R.id.recyclerView)
        submitButton = findViewById(R.id.submitButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchPeople()

        submitButton.setOnClickListener {
            val selected = adapter.getUpdatedList().filter { it.isPresent != null }
            sendAttendance(selected)
        }
    }

    private fun fetchPeople() {
        val client = OkHttpClient()
        val url = if (type == "student")
            "http://10.0.2.2:3000/get-students/$schoolId"
        else
            "http://10.0.2.2:3000/cooking-staff-details/$schoolId"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AttendanceActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonArray = JSONArray(response.body?.string())
                val people = mutableListOf<Student>()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    people.add(
                        Student(
                            id = obj.optString("userId", obj.optString("id")),
                            name = obj.getString("name"),
                            `class` = obj.optString("class", "N/A")
                        )
                    )
                }

                runOnUiThread {
                    adapter = StudentAdapter(people)
                    recyclerView.adapter = adapter
                }
            }
        })
    }

    private fun sendAttendance(people: List<Student>) {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaTypeOrNull()
        val resultArray = JSONArray()

        for (person in people) {
            val obj = JSONObject()
            obj.put("schoolId", schoolId)
            obj.put("userId", person.id)
            obj.put("class", person.`class`)
            obj.put("present", person.isPresent)
            resultArray.put(obj)
        }

        val url = if (type == "student")
            "http://10.0.2.2:3000/add-attendance"
        else
            "http://10.0.2.2:3000/add-cooking-attendance"

        val requestBody = RequestBody.create(mediaType, resultArray.toString())
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AttendanceActivity, "Failed to submit", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(this@AttendanceActivity, "Submitted successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
