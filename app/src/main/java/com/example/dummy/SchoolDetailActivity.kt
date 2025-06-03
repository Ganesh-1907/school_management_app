package com.example.dummy

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SchoolDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_detail)

        val schoolId = intent.getStringExtra("schoolId")
        if (schoolId != null) {
            fetchSchoolDetails(schoolId)
        } else {
            Toast.makeText(this, "Invalid school ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchSchoolDetails(schoolId: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://school-management-app-five-drab.vercel.app/get-school/$schoolId")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SchoolDetailActivity, "Failed to fetch school", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val obj = JSONObject(responseBody)
                    runOnUiThread {
                        findViewById<TextView>(R.id.detailSchoolName).text = " ${obj.getString("schoolName")}"
                        findViewById<TextView>(R.id.detailSchoolAddress).text = " ${obj.getString("schoolAddress")}"
                        findViewById<TextView>(R.id.detailSchoolPhone).text = " ${obj.getString("schoolPhone")}"
                        findViewById<TextView>(R.id.detailSchoolEmail).text = "${obj.getString("schoolEmail")}"
                        findViewById<TextView>(R.id.detailSchoolType).text = " ${obj.getString("schoolType")}"
                        findViewById<TextView>(R.id.detailSchoolCategory).text = "${obj.getString("schoolCategory")}"
                        findViewById<TextView>(R.id.detailSchoolMedium).text = "${obj.getString("schoolMedium")}"
                        findViewById<TextView>(R.id.detailSchoolManagement).text = "${obj.getString("schoolManagement")}"
                        findViewById<TextView>(R.id.detailSchoolLevel).text = "${obj.getString("schoolLevel")}"
                        findViewById<TextView>(R.id.detailSchoolCode).text = "${obj.getString("schoolCode")}"
                        findViewById<TextView>(R.id.detailSchoolLocation).text = "${obj.getString("mandal")}, ${obj.getString("district")}, ${obj.getString("state")}, ${obj.getString("country")} - ${obj.getString("pincode")}"
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SchoolDetailActivity, "Error fetching school details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
