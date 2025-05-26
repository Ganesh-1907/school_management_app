package com.example.dummy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        recyclerView = findViewById(R.id.studentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(studentList)
        recyclerView.adapter = adapter

        loadDummyData()
    }

    private fun loadDummyData() {
        for (i in 1..20) {
            studentList.add(
                Student(
                    name = "Student $i",
                    role = "Student",
                    password = "pass$i",
                    `class` = "Class ${i % 10}",
                    phone = "987654321$i",
                    address = "Address $i",
                    dateOfBirth = "200$i-01-01",
                    gender = if (i % 2 == 0) "Male" else "Female",
                    motherName = "Mother $i",
                    fatherName = "Father $i"
                )
            )
        }
        adapter.notifyDataSetChanged()
    }

    class StudentAdapter(private val students: List<Student>) :
        RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

        class StudentViewHolder(val view: android.view.View) : RecyclerView.ViewHolder(view) {
            val name = view.findViewById<android.widget.TextView>(R.id.name)
            val className = view.findViewById<android.widget.TextView>(R.id.className)
            val phone = view.findViewById<android.widget.TextView>(R.id.phone)
            val address = view.findViewById<android.widget.TextView>(R.id.address)
            val dob = view.findViewById<android.widget.TextView>(R.id.dob)
            val gender = view.findViewById<android.widget.TextView>(R.id.gender)
            val mother = view.findViewById<android.widget.TextView>(R.id.motherName)
            val father = view.findViewById<android.widget.TextView>(R.id.fatherName)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): StudentViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
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
