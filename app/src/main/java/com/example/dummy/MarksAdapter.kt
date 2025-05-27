package com.example.dummy

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MarksAdapter(private val students: MutableList<StudentMarks>) :
    RecyclerView.Adapter<MarksAdapter.MarksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_marks, parent, false)
        return MarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    fun getUpdatedList(): MutableList<StudentMarks> = students

    inner class MarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.studentName)
        private val marksInput: EditText = itemView.findViewById(R.id.marksInput)

        fun bind(student: StudentMarks) {
            nameView.text = student.name

            // Prevent multiple triggers by clearing listener first
            marksInput.setText(student.marks?.toString() ?: "")

            marksInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    student.marks = s.toString().toIntOrNull()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }
}
