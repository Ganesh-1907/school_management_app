package com.example.dummy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Warden_Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_warden_dashboard)
        val schoolId = intent.getStringExtra("schoolId")
        Log.d("WardenDashboard", "Received schoolId: $schoolId")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val commodityIndentCard = findViewById<androidx.cardview.widget.CardView>(R.id.card3_Commodity_indent_warden)
        commodityIndentCard.setOnClickListener {
            val intent = Intent(this, CommodityIndentMonthlyWardenActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val cookingStaffCard = findViewById<androidx.cardview.widget.CardView>(R.id.card8_Cooking_Staff_user_add_warden)
        cookingStaffCard.setOnClickListener {
            val intent = Intent(this, Add_User_page_officer::class.java)
            intent.putExtra("source", "cooking")
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val commodityReceiptsCard = findViewById<androidx.cardview.widget.CardView>(R.id.card4_Commodity_receipts)
        commodityReceiptsCard.setOnClickListener {
            val intent = Intent(this, SchoolCommodityActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val studentHealthStatusCard = findViewById<androidx.cardview.widget.CardView>(R.id.card6_Student_Health_Status_warden)
        studentHealthStatusCard.setOnClickListener {
            val intent = Intent(this, HealthReportsActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

        val viewAnnouncementCard = findViewById<androidx.cardview.widget.CardView>(R.id.card7_announcments_warden)
        viewAnnouncementCard.setOnClickListener {
            val intent = Intent(this, AnnouncementActivity::class.java)
            intent.putExtra("schoolId", schoolId)
            startActivity(intent)
        }

    }
}