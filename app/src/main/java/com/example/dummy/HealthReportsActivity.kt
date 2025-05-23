// HealthReportsActivity.kt
package com.example.dummy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.adapters.HealthReportAdapter
import com.example.dummy.models.HealthReport
import okhttp3.*
import android.view.View
import android.widget.TextView
import org.json.JSONArray
import java.io.IOException

class HealthReportsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HealthReportAdapter
    private lateinit var noRecordsText: TextView
    private val healthList = mutableListOf<HealthReport>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_reports)

        val schoolId = intent.getStringExtra("schoolId")
        if (schoolId == null) {
            Toast.makeText(this, "School ID is missing!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.healthRecyclerView)
        noRecordsText = findViewById(R.id.noRecordsText)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HealthReportAdapter(healthList)
        recyclerView.adapter = adapter

        fetchHealthReports(schoolId)
    }

    private fun fetchHealthReports(schoolId: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:3000/get-students-health/$schoolId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@HealthReportsActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonArray = JSONArray(responseBody)
                    healthList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val report = HealthReport(
                            userName = obj.getString("userName"),
                            healthStatus = obj.getString("healthStatus")
                        )
                        healthList.add(report)
                    }

                    runOnUiThread {
                        if (healthList.isEmpty()) {
                            noRecordsText.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        } else {
                            noRecordsText.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}
