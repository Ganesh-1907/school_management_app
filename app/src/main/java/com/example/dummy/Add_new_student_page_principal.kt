package com.example.dummy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class AddNewStudentPagePrincipalActivity : AppCompatActivity() {

    private lateinit var classSpinner: Spinner
    private lateinit var submitButton: Button

    private lateinit var studentNameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var fatherNameEditText: EditText
    private lateinit var motherNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText

    private var schoolId: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_new_student_page_principal)

        // Get schoolId from intent extras
        schoolId = intent.getStringExtra("schoolId")

        classSpinner = findViewById(R.id.class_spinner)
        submitButton = findViewById(R.id.add_student_submit_button_principal)

        studentNameEditText = findViewById(R.id.student_name_enter_principal)
        dobEditText = findViewById(R.id.Student_age_enter_principal)
        genderRadioGroup = findViewById(R.id.Student_Gender_enter_principal)
        fatherNameEditText = findViewById(R.id.student_father_name_enter_principal)
        motherNameEditText = findViewById(R.id.student_mother_name_enter_principal)
        phoneEditText = findViewById(R.id.student_parent_phoneNumber_enter_principal)
        addressEditText = findViewById(R.id.address_student_enter_principal)

        val classes = arrayOf(
            "Class 1", "Class 2", "Class 3", "Class 4", "Class 5",
            "Class 6", "Class 7", "Class 8", "Class 9", "Class 10"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classSpinner.adapter = adapter

        submitButton.setOnClickListener {
            submitStudentData()
        }
    }

    private fun submitStudentData() {
        val name = studentNameEditText.text.toString().trim()
        val dob = dobEditText.text.toString().trim()
        val fatherName = fatherNameEditText.text.toString().trim()
        val motherName = motherNameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()

        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        val genderRadioButton = findViewById<RadioButton>(selectedGenderId)
        val gender = genderRadioButton?.text?.toString() ?: ""

        val selectedClass = classSpinner.selectedItem.toString()

        // Build JSON payload with only required fields + schoolId from intent
        val json = JSONObject()
        json.put("name", name)
        json.put("dateOfBirth", dob)
        json.put("fatherName", fatherName)
        json.put("motherName", motherName)
        json.put("phone", phone)
        json.put("address", address)
        json.put("gender", gender)
        json.put("class", selectedClass)
        json.put("schoolId", schoolId ?: "1234")  // Use empty string if null

        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
            .url("https://school-management-app-five-drab.vercel.app/add-student")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AddNewStudentPagePrincipalActivity, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddNewStudentPagePrincipalActivity, "Student added successfully", Toast.LENGTH_LONG).show()
//                        clearForm()
                    } else {
                        Toast.makeText(this@AddNewStudentPagePrincipalActivity, "Error: $responseText", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

//    private fun clearForm() {
//        studentNameEditText.text.clear()
//        dobEditText.text.clear()
//        fatherNameEditText.text.clear()
//        motherNameEditText.text.clear()
//        phoneEditText.text.clear()
//        addressEditText.text.clear()
//        genderRadioGroup.clearCheck()
//        classSpinner.setSelection(0)
//    }
}
