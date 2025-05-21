package com.example.dummy

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dummy.R
import java.util.*
import okhttp3.*
import okhttp3.MediaType.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class Add_User_page_officer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_user_page_officer)

        val name = findViewById<EditText>(R.id.Employee_name_enter_officer)
        val dob = findViewById<EditText>(R.id.Employee_age_enter_officer)
        val genderGroup = findViewById<RadioGroup>(R.id.Employee_Gender_enter_officer)
        val role = findViewById<Spinner>(R.id.Employee_role_enter_officer)
        val email = findViewById<EditText>(R.id.Employee_email_id_enter_officer)
        val mobile = findViewById<EditText>(R.id.Employee_mobile_number_enter_officer)
        val joiningDate = findViewById<EditText>(R.id.Employee_joining_date_enter_officer)
        val address = findViewById<EditText>(R.id.Employee_address_add_officer)
        val submitBtn = findViewById<Button>(R.id.submit_button)

        // Setup Spinner
        val roles = listOf("Select Role", "Principal", "Teacher", "Warden")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        role.adapter = adapter

        // Show Date Picker Dialog for DOB
        dob.setOnClickListener {
            showDatePicker(dob)
        }

        // Show Date Picker Dialog for Joining Date
        joiningDate.setOnClickListener {
            showDatePicker(joiningDate)
        }


        submitBtn.setOnClickListener {
            val selectedGenderId = genderGroup.checkedRadioButtonId
            val selectedGender = findViewById<RadioButton>(selectedGenderId)?.text.toString()

            val jsonBody = JSONObject()
            jsonBody.put("name", name.text.toString())
            jsonBody.put("email", email.text.toString())
            jsonBody.put("role", role.selectedItem.toString())
//            jsonBody.put("password", "default123") // Replace with user input if needed
            jsonBody.put("phone", mobile.text.toString())
            jsonBody.put("address", address.text.toString())
            jsonBody.put("dateOfBirth", dob.text.toString())
            jsonBody.put("gender", selectedGender)
            jsonBody.put("joiningDate", joiningDate.text.toString())

            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            val body = RequestBody.create(JSON, jsonBody.toString())

            // 👇 Access localhost on your computer from the Android Emulator
            val request = Request.Builder()
                .url("http://10.0.2.2:3000/add-user")
                .post(body)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@Add_User_page_officer, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()
                    runOnUiThread {
                        Toast.makeText(this@Add_User_page_officer, "Response: $res", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            val selectedDate = "${"%02d".format(d)}/${"%02d".format(m + 1)}/$y"
            editText.setText(selectedDate)
        }, year, month, day)

        dpd.show()
    }
}
