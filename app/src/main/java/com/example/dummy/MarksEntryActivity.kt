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

class MarksEntryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var submitButton: Button
    private lateinit var adapter: MarksAdapter
    private var schoolId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marks)

        schoolId = intent.getStringExtra("schoolId") ?: ""
        recyclerView = findViewById(R.id.recyclerView)
        submitButton = findViewById(R.id.submitButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchStudents()

        submitButton.setOnClickListener {
            val marksList = adapter.getUpdatedList().filter { it.marks != null }
            sendMarks(marksList)
        }
    }

    private fun fetchStudents() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://school-management-app-five-drab.vercel.app/get-students/$schoolId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MarksEntryActivity, "Failed to load students", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonArray = JSONArray(response.body?.string())
                val students = mutableListOf<StudentMarks>()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    students.add(
                        StudentMarks(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            `class` = obj.getString("class")
                        )
                    )
                }

                runOnUiThread {
                    adapter = MarksAdapter(students)
                    recyclerView.adapter = adapter
                }
            }
        })
    }

    private fun sendMarks(students: List<StudentMarks>) {
        val client = OkHttpClient()
        val mediaType = "application/json".toMediaTypeOrNull()
        val resultArray = JSONArray()

        for (student in students) {
            val obj = JSONObject()
            obj.put("schoolId", schoolId)
            obj.put("userId", student.id)
            obj.put("class", student.`class`)
            obj.put("marks", student.marks)
            resultArray.put(obj)
        }

        val requestBody = RequestBody.create(mediaType, resultArray.toString())
        val request = Request.Builder()
            .url("https://school-management-app-five-drab.vercel.app/add-marks")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MarksEntryActivity, "Failed to submit marks", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(this@MarksEntryActivity, "Marks submitted successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
