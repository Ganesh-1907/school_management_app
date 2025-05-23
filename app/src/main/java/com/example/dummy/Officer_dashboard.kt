package com.example.dummy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dummy.databinding.ActivityOfficerDashboardBinding

class Officer_dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_officer_dashboard)

        // Handle insets for fullscreen layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the card and set click listener

        val mapSchoolsCard = findViewById<androidx.cardview.widget.CardView>(R.id.card1_mapschools_officer)
        mapSchoolsCard.setOnClickListener {
            val intent = Intent(this, SchoolListActivity::class.java)
            intent.putExtra("source", "map")
            startActivity(intent)
        }

        val addUserCard = findViewById<androidx.cardview.widget.CardView>(R.id.card2_adduser_officer)
        addUserCard.setOnClickListener {
            val intent = Intent(this, Add_User_page_officer::class.java)
            startActivity(intent)
        }

        val schoolCommodityCard = findViewById<androidx.cardview.widget.CardView>(R.id.card3_school_commodity_officer)
        schoolCommodityCard.setOnClickListener {
            val intent = Intent(this, SchoolListActivity::class.java)
            intent.putExtra("source", "commodities")
            startActivity(intent)
        }

        val schoolDetailsCard = findViewById<androidx.cardview.widget.CardView>(R.id.card4_school_details_officer)
        schoolDetailsCard.setOnClickListener {
            val intent = Intent(this, SchoolListActivity::class.java)
            intent.putExtra("source", "school")
            startActivity(intent)
        }

        val studentHealthRecordsCard = findViewById<androidx.cardview.widget.CardView>(R.id.card5_student_health_records)
        studentHealthRecordsCard.setOnClickListener {
            val intent = Intent(this, SchoolListActivity::class.java)
            intent.putExtra("source", "reports")
            startActivity(intent)
        }

    }
}
