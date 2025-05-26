package com.example.dummy

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.dummy.databinding.ActivityTimetablesBinding

class TimetablesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimetablesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetablesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Static sample timetables
        val timetables = listOf(
            "Class 1\nMath\nEnglish\nBreak\nEVS\nGames",
            "Class 2\nEnglish\nMath\nBreak\nScience\nDrawing",
            "Class 3\nMath\nEnglish\nBreak\nEVS\nMusic",
            "Class 4\nScience\nEnglish\nBreak\nMath\nPT",
            "Class 5\nMath\nEnglish\nBreak\nHistory\nArt",
            "Class 6\nGeography\nScience\nBreak\nMath\nHindi",
            "Class 7\nEnglish\nChemistry\nBreak\nMath\nBiology",
            "Class 8\nPhysics\nMath\nBreak\nEnglish\nComputer",
            "Class 9\nMaths\nBiology\nBreak\nEnglish\nChemistry",
            "Class 10\nPhysics\nMath\nBreak\nComputer\nPE"
        )

        val container = binding.tableContainer

        for (i in timetables.indices) {
            val classTitle = android.widget.TextView(this).apply {
                text = "Class ${i + 1} Timetable"
                textSize = 18f
                setPadding(8, 16, 8, 8)
                setTypeface(null, android.graphics.Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val timetable = android.widget.TextView(this).apply {
                text = timetables[i]
                setPadding(16, 8, 16, 24)
                // Remove the problematic background or use a simple one
                setBackgroundResource(android.R.color.white)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16)
                }
            }

            container.addView(classTitle)
            container.addView(timetable)
        }
    }
}