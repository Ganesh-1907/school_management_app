package com.example.dummy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class StudentDetailsActivity : AppCompatActivity() {

    data class Student(
        val name: String,
        val role: String,
        val password: String,
        val `class`: String,
        val phone: String,
        val address: String,
        val dateOfBirth: String,
        val gender: String,
        val motherName: String,
        val fatherName: String
    )

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student>()
    private var schoolId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        schoolId = intent.getStringExtra("schoolId") ?: ""
        Log.d("StudentDetailsActivity", "Received schoolId: $schoolId")

        recyclerView = findViewById(R.id.studentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(studentList)
        recyclerView.adapter = adapter

        if (schoolId.isNotEmpty()) {
            fetchStudentData()
        } else {
            Log.e("StudentDetailsActivity", "No schoolId received from intent.")
        }
    }

    private fun fetchStudentData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://school-management-app-five-drab.vercel.app/get-students/$schoolId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API_CALL", "Failed to fetch students: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { json ->
                    val listType = object : TypeToken<List<Student>>() {}.type
                    val students: List<Student> = Gson().fromJson(json, listType)

                    runOnUiThread {
                        studentList.clear()
                        studentList.addAll(students)
                        adapter.notifyDataSetChanged()
                        Log.d("API_CALL", "Successfully fetched ${students.size} students.")
                    }
                } ?: Log.e("API_CALL", "Empty response body.")
            }
        })
    }

    class StudentAdapter(private val students: List<Student>) :
        RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

        class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.name)
            val className: TextView = view.findViewById(R.id.className)
            val phone: TextView = view.findViewById(R.id.phone)
            val address: TextView = view.findViewById(R.id.address)
            val dob: TextView = view.findViewById(R.id.dob)
            val gender: TextView = view.findViewById(R.id.gender)
            val mother: TextView = view.findViewById(R.id.motherName)
            val father: TextView = view.findViewById(R.id.fatherName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.student_item, parent, false)
            return StudentViewHolder(view)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            val s = students[position]
            holder.name.text = "Name: ${s.name}"
            holder.className.text = "Class: ${s.`class`}"
            holder.phone.text = "Phone: ${s.phone}"
            holder.address.text = "Address: ${s.address}"
            holder.dob.text = "DOB: ${s.dateOfBirth}"
            holder.gender.text = "Gender: ${s.gender}"
            holder.mother.text = "Mother: ${s.motherName}"
            holder.father.text = "Father: ${s.fatherName}"
        }

        override fun getItemCount(): Int = students.size
    }
}
