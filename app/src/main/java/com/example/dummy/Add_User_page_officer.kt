package com.example.dummy

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.*

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

        val source = intent.getStringExtra("source") ?: ""
        val schoolId = intent.getStringExtra("schoolId") ?: ""

        val roles = listOf("Select Role", "Principal", "Teacher", "Warden", "Cooking Staff")
        role.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        if (source == "cooking") role.apply { setSelection(roles.indexOf("Cooking Staff")); isEnabled = false }

        dob.setOnClickListener { showDatePicker(dob) }
        joiningDate.setOnClickListener { showDatePicker(joiningDate) }

        submitBtn.setOnClickListener {
            val selectedGender = findViewById<RadioButton>(genderGroup.checkedRadioButtonId)?.text.toString()
            val selectedRole = role.selectedItem.toString()

            val jsonBody = JSONObject().apply {
                put("name", name.text.toString())
                put("email", email.text.toString())
                put("role", selectedRole)
                put("phone", mobile.text.toString())
                put("address", address.text.toString())
                put("dateOfBirth", dob.text.toString())
                put("gender", selectedGender)
                put("joiningDate", joiningDate.text.toString())
                if (selectedRole == "Cooking Staff") put("schoolId", schoolId)
            }

            val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonBody.toString())

            OkHttpClient().newCall(
                Request.Builder()
                    .url("http://10.0.2.2:3000/add-user")
                    .post(body)
                    .build()
            ).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) = runOnUiThread {
                    Toast.makeText(this@Add_User_page_officer, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call, response: Response) = runOnUiThread {
                    Toast.makeText(this@Add_User_page_officer, "Response: ${response.body?.string()}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun showDatePicker(editText: EditText) {
        Calendar.getInstance().run {
            DatePickerDialog(
                this@Add_User_page_officer,
                { _, y, m, d -> editText.setText("%02d/%02d/%d".format(d, m + 1, y)) },
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
