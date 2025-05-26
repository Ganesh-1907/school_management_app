package com.example.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val studentList: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.studentName)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
        val yes: RadioButton = itemView.findViewById(R.id.radioYes)
        val no: RadioButton = itemView.findViewById(R.id.radioNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student.name

        holder.radioGroup.setOnCheckedChangeListener(null)

        holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            student.isPresent = when (checkedId) {
                R.id.radioYes -> true
                R.id.radioNo -> false
                else -> null
            }
        }
    }

    fun getUpdatedList(): List<Student> = studentList
}
