package com.example.dummy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SchoolMapping : AppCompatActivity() {

    private lateinit var roleSpinner: Spinner
    private lateinit var userSpinner: Spinner
    private lateinit var submitButton: Button

    private var schoolId: String? = null
    private var selectedRole: String? = null
    private var selectedUserId: String? = null

    private val client = OkHttpClient()

    private val roles = listOf("Teacher", "Principal", "Warden")
    private val usersMap = mutableMapOf<String, String>() // userName -> userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_mapping)

        roleSpinner = findViewById(R.id.roleSpinner)
        userSpinner = findViewById(R.id.userSpinner)
        submitButton = findViewById(R.id.submitButton)

        schoolId = intent.getStringExtra("schoolId")

        // Setup role spinner
        val roleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = roleAdapter

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedRole = roles[position]
                fetchUsersForRole(selectedRole!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val userName = parent.getItemAtPosition(position) as String
                selectedUserId = usersMap[userName]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedUserId = null
            }
        }

        submitButton.setOnClickListener {
            if (schoolId == null) {
                Toast.makeText(this, "School ID is missing!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedRole == null || selectedUserId == null) {
                Toast.makeText(this, "Please select both role and user", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mapSchoolWithUser(schoolId!!, selectedUserId!!, selectedRole!!)
        }
    }

    private fun fetchUsersForRole(role: String) {
        val url = "https://school-management-app-five-drab.vercel.app/get-users/$role"
        val request = Request.Builder().url(url).get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SchoolMapping, "Failed to fetch users: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (response.isSuccessful && body != null) {
                    val jsonArray = JSONArray(body)
                    usersMap.clear()
                    val userNames = mutableListOf<String>()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("id")
                        val name = obj.getString("name")
                        usersMap[name] = id
                        userNames.add(name)
                    }

                    runOnUiThread {
                        val userAdapter = ArrayAdapter(this@SchoolMapping, android.R.layout.simple_spinner_item, userNames)
                        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        userSpinner.adapter = userAdapter
                        if (userNames.isNotEmpty()) {
                            selectedUserId = usersMap[userNames[0]]
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SchoolMapping, "No users found for role: $role", Toast.LENGTH_SHORT).show()
                        userSpinner.adapter = null
                    }
                }
            }
        })
    }

    private fun mapSchoolWithUser(schoolId: String, userId: String, role: String) {
        val url = "https://school-management-app-five-drab.vercel.app/map-school-with-user"

        val jsonBody = JSONObject()
        jsonBody.put("schoolId", schoolId)
        jsonBody.put("userId", userId)
        jsonBody.put("role", role)

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), jsonBody.toString())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SchoolMapping, "Failed to map school with user: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val respBody = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SchoolMapping, "Mapped successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SchoolMapping, "Failed to map: $respBody", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
