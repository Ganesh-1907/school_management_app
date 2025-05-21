import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.R

class SchoolAdapter(
    private val schools: List<School>,
    private val onItemClick: (School) -> Unit
) : RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder>() {

    inner class SchoolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schoolName: TextView = itemView.findViewById(R.id.schoolNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.school_item, parent, false)
        return SchoolViewHolder(view)
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        val school = schools[position]
        holder.schoolName.text = school.schoolName
        holder.itemView.setOnClickListener {
            onItemClick(school)
        }
    }

    override fun getItemCount(): Int = schools.size
}
