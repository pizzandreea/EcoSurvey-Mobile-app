package com.example.surveyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.Classes.UserAdapter
import com.example.surveyapp.Classes.UserData
import java.util.*
import kotlin.collections.ArrayList

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<UserData>()
    private lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        recyclerView = findViewById(R.id.recycleViewLeaderboard)
        searchView = findViewById(R.id.searchViewLeaderboard)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)


        addDataTpList()
        adapter = UserAdapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }



        })
    }

    private fun filterList(query : String?) {
        if(query != null){
            val filteredList = ArrayList<UserData>()
            for(item in mList){
                if(item.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))){
                    filteredList.add(item)
                }
            }
            if(filteredList.isEmpty()) {
                Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show()
            }
            else{
                adapter.filterList(filteredList)
            }
        }
    }
    private fun addDataTpList() {
        mList.add(UserData("John", R.drawable.profile1, 230))
        mList.add(UserData("Mary", R.drawable.profile2, 100))
        mList.add(UserData("Peter", R.drawable.profile3, 50))
        mList.add(UserData("Nimeni", R.drawable.profile1, 0))
        mList.add(UserData("Nimeni", R.drawable.profile1, 0))
        mList.add(UserData("Nimeni", R.drawable.profile1, 0))
        mList.add(UserData("Nimeni", R.drawable.profile1, 0))
        mList.add(UserData("Nimeni", R.drawable.profile1, 0))
        mList.add(UserData("Nimeni", R.drawable.profile1, 0))

    }
}