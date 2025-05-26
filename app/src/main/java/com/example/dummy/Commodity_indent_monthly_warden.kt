package com.example.dummy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class CommodityIndentMonthlyWardenActivity : AppCompatActivity() {

    private lateinit var monthSpinner: Spinner
    private lateinit var itemSpinner: Spinner
    private lateinit var phase1Edit: EditText
    private lateinit var phase2Edit: EditText
    private lateinit var btnSubmit: Button

    // schoolId received from Intent, nullable
    private var schoolId: String? = null

    private val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    private val items = listOf("rice", "chikki", "egg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_commodity_indent_monthly_warden)

        // Receive schoolId from Intent
        schoolId = intent.getStringExtra("schoolId")
        if (schoolId == null) {
            Toast.makeText(this, "No schoolId received!", Toast.LENGTH_SHORT).show()
        } else {
            android.util.Log.d("CommodityIndent", "Received schoolId: $schoolId")
        }

        monthSpinner = findViewById(R.id.spinnerMonth)
        itemSpinner = findViewById(R.id.spinnerItem)
        phase1Edit = findViewById(R.id.editPhase1)
        phase2Edit = findViewById(R.id.editPhase2)
        btnSubmit = findViewById(R.id.btnSubmit)

        monthSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)

        itemSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        btnSubmit.setOnClickListener {
            sendDataToApi()
        }
    }

    private fun sendDataToApi() {
        val selectedMonth = monthSpinner.selectedItem.toString()
        val selectedItem = itemSpinner.selectedItem.toString()
        val phase1 = phase1Edit.text.toString()
        val phase2 = phase2Edit.text.toString()

        if (schoolId.isNullOrEmpty()) {
            Toast.makeText(this, "School ID is missing!", Toast.LENGTH_SHORT).show()
            return
        }

        val json = JSONObject().apply {
            put("schoolId", schoolId)
            put("month", selectedMonth)
            put("phase1", phase1)
            put("phase2", phase2)
            put("title", selectedItem)
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url("http://10.0.2.2:3000/set-student-commodity")
            .post(body)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@CommodityIndentMonthlyWardenActivity, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                runOnUiThread {
                    Toast.makeText(this@CommodityIndentMonthlyWardenActivity, "Success: $result", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
