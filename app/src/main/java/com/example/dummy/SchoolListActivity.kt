package com.example.dummy

import School
import SchoolAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class SchoolListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var schoolAdapter: SchoolAdapter
    private val schools = mutableListOf<School>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_list)

        val source = intent.getStringExtra("source") ?: "default"

        recyclerView = findViewById(R.id.schoolRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val intent = when (source) {
            "map" -> Intent(this, SchoolMapping::class.java)
            "commodities" -> Intent(this, SchoolCommodityActivity::class.java)
            "school" -> Intent(this, SchoolDetailActivity::class.java)
            "reports" -> Intent(this, HealthReportsActivity::class.java)
            else -> Intent(this, SchoolDetailActivity::class.java) // fallback
        }
        schoolAdapter = SchoolAdapter(schools) { selectedSchool ->
            intent.putExtra("schoolId", selectedSchool.id)
            startActivity(intent)
        }

        recyclerView.adapter = schoolAdapter
        fetchSchoolsFromApi()
    }

    private fun fetchSchoolsFromApi() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:3000/get-schools")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SchoolListActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonArray = JSONArray(responseBody)
                    schools.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val school = School(
                            id = obj.getString("id"),
                            schoolName = obj.getString("schoolName"),
                            schoolAddress = obj.getString("schoolAddress"),
                            schoolPhone = obj.getString("schoolPhone"),
                            schoolEmail = obj.getString("schoolEmail"),
                            mandal = obj.getString("mandal"),
                            district = obj.getString("district"),
                            state = obj.getString("state"),
                            country = obj.getString("country"),
                            pincode = obj.getString("pincode"),
                            schoolType = obj.getString("schoolType"),
                            schoolCategory = obj.getString("schoolCategory"),
                            schoolMedium = obj.getString("schoolMedium"),
                            schoolManagement = obj.getString("schoolManagement"),
                            schoolLevel = obj.getString("schoolLevel"),
                            schoolCode = obj.getString("schoolCode")
                        )
                        schools.add(school)
                    }
                    runOnUiThread {
                        schoolAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
