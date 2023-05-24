package com.example.surveyapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private var database: FirebaseDatabase? = null

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
//        addDataTpList()
        mList = arrayListOf<UserData>()
        try {
            database = FirebaseDatabase.getInstance("https://surveyapp-837e7-default-rtdb.europe-west1.firebasedatabase.app/")
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreate: ", e)
        }
        val myRef = database?.reference?.child("users")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    Log.d("TAGfullnameleaderboard", ds.child("fullName").value.toString())
                    val name = ds.child("fullName").value.toString()
                    val score = ds.child("score").value.toString().toInt()
                    val imageId = R.drawable.profile1
                    mList.add(UserData(name, imageId, score))
                    Log.d("TAGmList", mList.toString())
                }
                mList.sortByDescending { it.score }
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

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "onCancelled: ", error.toException())
            }
        }
        myRef?.addListenerForSingleValueEvent(valueEventListener)





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