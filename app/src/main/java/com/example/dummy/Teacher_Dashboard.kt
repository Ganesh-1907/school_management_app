package com.example.dummy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Teacher_Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_dashboard)
        val schoolId = intent.getStringExtra("schoolId")
        Log.d("TeacherDashboard", "Received schoolId: $schoolId")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val announcementsCard = findViewById<androidx.cardview.widget.CardView>(R.id.card4_View_announcments_teacher)
        announcementsCard.setOnClickListener {
            val intent = Intent(this, AnnouncementActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val addMarksCard = findViewById<androidx.cardview.widget.CardView>(R.id.card2_Enter_student_marks_teacher)
        addMarksCard.setOnClickListener {
            val intent = Intent(this, MarksEntryActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            intent.putExtra("entryType", "marks")
            startActivity(intent)
        }

        val addAttendenceCard = findViewById<androidx.cardview.widget.CardView>(R.id.card1_Student_attendence_teacher)
        addAttendenceCard.setOnClickListener {
            val intent = Intent(this, AttendanceActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            intent.putExtra("type", "student")
            startActivity(intent)
        }

        val timeTableCard = findViewById<androidx.cardview.widget.CardView>(R.id.card3_Timetable_Teacher)
        timeTableCard.setOnClickListener {
            val intent = Intent(this, TimetablesActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val healthStatusCard = findViewById<androidx.cardview.widget.CardView>(R.id.card5_student_health_monitoring_teacher)
        healthStatusCard.setOnClickListener {
            val intent = Intent(this, HealthReportsActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val studentDetailsCard = findViewById<androidx.cardview.widget.CardView>(R.id.card6_student_Details_teacher)
        studentDetailsCard.setOnClickListener {
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

    }
}