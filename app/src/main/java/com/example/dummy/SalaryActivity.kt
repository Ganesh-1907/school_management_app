package com.example.dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SalaryActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var btnSubmit: Button

    private val client = OkHttpClient()
    private val schoolId = "bMIvw9HgYtpzRXwLHsee" // your schoolId here
    private val staffList = mutableListOf<Staff>()

    data class Staff(val id: String, val userId: String, val name: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salary)

        container = findViewById(R.id.container)
        btnSubmit = findViewById(R.id.btnSubmit)

        fetchStaffDetails()

        btnSubmit.setOnClickListener {
            submitSalaries()
        }
    }

    private fun fetchStaffDetails() {
        val url = "http://10.0.2.2:3000/cooking-staff-details/$schoolId"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SalaryActivity, "Failed to load staff", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { body ->
                    val jsonArray = JSONArray(body)
                    staffList.clear()
                    runOnUiThread {
                        container.removeAllViews()
                    }
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val staff = Staff(
                            obj.getString("id"),
                            obj.getString("userId"),
                            obj.getString("name")
                        )
                        staffList.add(staff)

                        runOnUiThread {
                            addStaffView(staff)
                        }
                    }
                }
            }
        })
    }

    private fun addStaffView(staff: Staff) {
        val inflater = LayoutInflater.from(this)
        val staffItem = inflater.inflate(R.layout.item_staff_salary, container, false)
        val tvName = staffItem.findViewById<TextView>(R.id.tvName)
        val etSalary = staffItem.findViewById<EditText>(R.id.etSalary)

        tvName.text = staff.name
        staffItem.tag = Pair(staff.userId, etSalary)  // store userId and EditText for later

        container.addView(staffItem)
    }

    private fun submitSalaries() {
        val jsonArray = JSONArray()
        val schoolId = this.schoolId

        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            val tag = child.tag as? Pair<*, *>
            if (tag != null) {
                val userId = tag.first as String
                val etSalary = tag.second as EditText
                val salary = etSalary.text.toString().trim()

                // Check empty salary
                if (salary.isEmpty()) {
                    Toast.makeText(this, "Salary cannot be empty for staff $userId", Toast.LENGTH_SHORT).show()
                    return  // stop submission on error
                }

                // Check valid number
                val salaryNum = salary.toDoubleOrNull()
                if (salaryNum == null) {
                    Toast.makeText(this, "Invalid salary input for staff $userId", Toast.LENGTH_SHORT).show()
                    return  // stop submission on error
                }

                val obj = JSONObject()
                obj.put("schoolId", schoolId)
                obj.put("userId", userId)
                obj.put("class", "")  // send empty as per your API
                obj.put("marks", salaryNum)  // send number, not string
                jsonArray.put(obj)
            }
        }

        if (jsonArray.length() == 0) {
            Toast.makeText(this, "Enter at least one salary", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://10.0.2.2:3000/add-staff-salary"
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonArray.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SalaryActivity, "Failed to submit salaries", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SalaryActivity, "Salaries submitted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SalaryActivity, "Submission failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
