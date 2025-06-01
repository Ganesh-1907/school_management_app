package com.example.dummy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummy.adapter.AnnouncementAdapter
import com.example.dummy.model.Announcement
import okhttp3.*
import java.io.IOException
import com.google.gson.Gson

class AnnouncementActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter

    private var schoolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcements)
        schoolId = intent.getStringExtra("schoolId")

        recyclerView = findViewById(R.id.recyclerViewAnnouncements)
        recyclerView.layoutManager = LinearLayoutManager(this)

        schoolId?.let { fetchAnnouncements(it) }
    }

    private fun fetchAnnouncements(schoolId: String) {
        val url = "https://school-management-app-five-drab.vercel.app/get-announcements/$schoolId"  // Replace with actual server URL

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val gson = Gson()
                    val announcementList = gson.fromJson(it, Array<Announcement>::class.java).toList()

                    runOnUiThread {
                        adapter = AnnouncementAdapter(announcementList)
                        recyclerView.adapter = adapter
                    }
                }
            }
        })
    }
}
