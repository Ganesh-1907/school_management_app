package com.example.dummy

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class StudentHealthActivity : AppCompatActivity() {

    data class Student(
        val id: String,
        val name: String,
        val `class`: String
    )

    data class HealthEntry(
        val studentId: String,
        val schoolId: String,
        val healthStatus: String
    )

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private var studentList = mutableListOf<Student>()
    private var schoolId: String = ""
    private val healthStatuses = listOf("healthy", "sick", "recovering", "critical")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_health)

        // Get schoolId passed via intent from previous activity
        schoolId = intent.getStringExtra("schoolId") ?: ""
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pass schoolId to adapter so it can use it later when submitting
        adapter = StudentAdapter(studentList, healthStatuses, schoolId)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            submitHealthData()
        }

        if (schoolId.isNotEmpty()) {
            fetchStudents()
        } else {
            Toast.makeText(this, "Missing schoolId", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchStudents() {
        val client = OkHttpClient()
        val url = "https://school-management-app-five-drab.vercel.app/get-students/$schoolId"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API", "Failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { body ->
                    val listType = object : TypeToken<List<Student>>() {}.type
                    val students: List<Student> = Gson().fromJson(body, listType)
                    runOnUiThread {
                        studentList.clear()
                        studentList.addAll(students)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun submitHealthData() {
        val selectedData = adapter.getSelectedStatuses().map {
            JSONObject().apply {
                put("studentId", it.studentId)
                put("schoolId", it.schoolId)
                put("healthStatus", it.healthStatus)
            }
        }

        val jsonArray = JSONArray(selectedData)
        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, jsonArray.toString())

        val request = Request.Builder()
            .url("https://school-management-app-five-drab.vercel.app/set-student-health")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("POST", "Failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@StudentHealthActivity, "Submitted Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@StudentHealthActivity, "Submit Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    class StudentAdapter(
        private val students: List<Student>,
        private val statuses: List<String>,
        private val schoolId: String
    ) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

        private val selectedStatusMap = mutableMapOf<String, String>()

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.studentName)
            val spinner: Spinner = view.findViewById(R.id.statusSpinner)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item_health, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val student = students[position]
            holder.nameText.text = student.name

            val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, statuses)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            holder.spinner.adapter = adapter

            holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    selectedStatusMap[student.id] = statuses[pos]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        override fun getItemCount() = students.size

        fun getSelectedStatuses(): List<HealthEntry> {
            return students.mapNotNull {
                val selected = selectedStatusMap[it.id]
                if (selected != null) {
                    HealthEntry(it.id, schoolId = schoolId, healthStatus = selected)
                } else null
            }
        }
    }
}
