package com.example.dummy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dummy.databinding.FragmentAddNewStudentPagePrincipalBinding

class Principal_Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal_dashboard)
        val schoolId = intent.getStringExtra("schoolId")
        Log.d("principal dashboard", "Received schoolId: $schoolId")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val studentDetailsCard = findViewById<CardView>(R.id.card1_Student_deatils_principal)
        studentDetailsCard.setOnClickListener {
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val postAnnouncementCard = findViewById<CardView>(R.id.card4_Post_announcments_Principal)
        postAnnouncementCard.setOnClickListener {
            val intent = Intent(this, Post_announcements_dailog_principal::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val addStudentCard = findViewById<CardView>(R.id.card2_add_student_principal)
        addStudentCard.setOnClickListener {
            val intent = Intent(this, FragmentAddNewStudentPagePrincipalBinding::class.java)
            intent.putExtra("source", "card")
            startActivity(intent)
        }

        val timeTablesCard = findViewById<CardView>(R.id.card3_Timetable_Principal)
        timeTablesCard.setOnClickListener {
            val intent = Intent(this, TimetablesActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val viewCommodityCard = findViewById<CardView>(R.id.card6_Commodity_details_principal)
        viewCommodityCard.setOnClickListener {
            val intent = Intent(this, SchoolCommodityActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val viewHealthCard = findViewById<CardView>(R.id.card5_Student_health_records_principal)
        viewHealthCard.setOnClickListener {
            val intent = Intent(this, HealthReportsActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val viewAnnouncementCard = findViewById<CardView>(R.id.card7_View_announcements_principal)
        viewAnnouncementCard.setOnClickListener {
            val intent = Intent(this, AnnouncementActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

    }
}