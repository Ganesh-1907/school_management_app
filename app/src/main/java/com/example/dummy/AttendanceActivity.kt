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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        schoolId = intent.getStringExtra("schoolId") ?: ""
        recyclerView = findViewById(R.id.recyclerView)
        submitButton = findViewById(R.id.submitButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchStudents()

        submitButton.setOnClickListener {
            val selected = adapter.getUpdatedList().filter { it.isPresent != null }
            sendAttendance(selected)
        }
    }

    private fun fetchStudents() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:3000/get-students/$schoolId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AttendanceActivity, "Failed to load", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonArray = JSONArray(response.body?.string())
                val students = mutableListOf<Student>()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    students.add(
                        Student(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            `class` = obj.getString("class")
                        )
                    )
                }

                runOnUiThread {
                    adapter = StudentAdapter(students)
                    recyclerView.adapter = adapter
                }
            }
        })
    }

    private fun sendAttendance(students: List<Student>) {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaTypeOrNull()
        val resultArray = JSONArray()

        for (student in students) {
            val obj = JSONObject()
            obj.put("schoolId", schoolId)
            obj.put("userId", student.id)
            obj.put("class", student.`class`)
            obj.put("present", student.isPresent)
            resultArray.put(obj)
        }

        val requestBody = RequestBody.create(mediaType, resultArray.toString())
        val request = Request.Builder()
            .url("http://10.0.2.2:3000/add-attendance")
            .post(requestBody)
            .build()

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
