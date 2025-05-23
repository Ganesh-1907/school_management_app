package com.example.dummy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class SchoolCommodityActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commodityAdapter: CommodityAdapter
    private val commodityList = mutableListOf<Commodity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_commodity)

        val schoolId = intent.getStringExtra("schoolId")
        if (schoolId == null) {
            Toast.makeText(this, "School ID missing!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.commodityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        commodityAdapter = CommodityAdapter(commodityList)
        recyclerView.adapter = commodityAdapter

        fetchCommodities(schoolId)
    }

    private fun fetchCommodities(schoolId: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://10.0.2.2:3000/get-students-commodity/$schoolId/")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SchoolCommodityActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonArray = JSONArray(responseBody)
                    commodityList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val commodity = Commodity(
                            id = obj.getString("id"),
                            schoolId = obj.getString("schoolId"),
                            month = obj.getString("month"),
                            phase1 = obj.getInt("phase1"),
                            phase2 = obj.getInt("phase2"),
                            title = obj.getString("title"),
                            createdAt = obj.getString("createdAt")
                        )
                        commodityList.add(commodity)
                    }
                    runOnUiThread {
                        commodityAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
