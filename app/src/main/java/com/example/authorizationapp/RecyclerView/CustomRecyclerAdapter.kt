package com.example.authorizationapp.RecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.authorizationapp.R
import com.example.authorizationapp.SubFolder.Subscription
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class CustomRecyclerAdapter(private val subs: ArrayList<Subscription>, private val context: Context): RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val costTextView: TextView = itemView.findViewById(R.id.costTextView)
        val currencyTextView: TextView = itemView.findViewById(R.id.currencyTextView)
        val firstDateTextView: TextView = itemView.findViewById(R.id.firstDateTextView)
        val lastDateTextView: TextView = itemView.findViewById(R.id.lastDateTextView)
        val daysTextView: TextView = itemView.findViewById(R.id.daysTextView)
        val subTypeTextView: TextView = itemView.findViewById(R.id.subTypeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var firstDate = LocalDate.now().format(formatter)
        var lastDate = LocalDate.parse(subs[position].lastDate, formatter)
        val resultDays: Long = ChronoUnit.DAYS.between(LocalDate.parse(firstDate, formatter), lastDate)
        holder.nameTextView.text = subs[position].title
        holder.costTextView.text = subs[position].cost.toString()
        holder.currencyTextView.text = subs[position].currency.printName()
        holder.firstDateTextView.text = subs[position].firstDate
        holder.lastDateTextView.text = subs[position].lastDate
        holder.daysTextView.text = "$resultDays"
        holder.subTypeTextView.text = subs[position].type.printName(context)
    }

    override fun getItemCount(): Int {
        return subs.size
    }
}