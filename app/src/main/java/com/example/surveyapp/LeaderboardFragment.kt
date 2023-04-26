package com.example.surveyapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.Classes.UserAdapter
import com.example.surveyapp.Classes.UserData
import java.util.*
import kotlin.collections.ArrayList

class LeaderboardFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var mList : ArrayList<UserData>
    private lateinit var adapter: UserAdapter
    lateinit var imageId : Array<Int>
    lateinit var name : Array<String>
    lateinit var score : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_leaderboard, container, false)
        // Inflate the layout for this fragment

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDataTpList()
        val layoutManager = LinearLayoutManager(context)

        recyclerView = view.findViewById(R.id.recycleViewLeaderboard)
        searchView = view.findViewById(R.id.searchViewLeaderboard)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

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
                Toast.makeText(context, "No match found", Toast.LENGTH_SHORT).show()
            }
            else{
                adapter.filterList(filteredList)
            }
        }
    }
    private fun addDataTpList() {

        mList = arrayListOf<UserData>()
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