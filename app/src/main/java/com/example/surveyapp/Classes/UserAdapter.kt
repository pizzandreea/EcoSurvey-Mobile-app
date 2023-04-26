package com.example.surveyapp.Classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import java.util.ArrayList

class UserAdapter(var mList: List<UserData>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    inner class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById<TextView>(R.id.userName)
        val score : TextView = itemView.findViewById<TextView>(R.id.userScore)
        val picture : ImageView = itemView.findViewById<ImageView>(R.id.profilePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.picture.setImageResource(mList[position].picture)
        holder.name.text = mList[position].name
        holder.score.text = mList[position].score.toString()
    }

    fun filterList(filteredList: ArrayList<UserData>) {
        this.mList = filteredList
        notifyDataSetChanged()
    }
}