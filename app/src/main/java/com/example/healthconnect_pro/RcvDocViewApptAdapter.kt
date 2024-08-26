package com.example.healthconnect_pro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RcvDocViewApptAdapter(private val appointmentList:ArrayList<Appointment>,private val context: Context,private val listener: OnItemClickListener):RecyclerView.Adapter<RcvDocViewApptAdapter.AppointmentDocViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int, appointment: Appointment)
    }

    class AppointmentDocViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val patName: TextView =itemview.findViewById(R.id.patNameInViewAppt)
        val aptTime: TextView =itemview.findViewById(R.id.ViewApptTimeDoc)
        val aptDate: TextView =itemview.findViewById(R.id.ViewApptDateDoc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentDocViewHolder {

        val itemview=
            LayoutInflater.from(parent.context).inflate(R.layout.rv_appointment_list_doc,parent,false)
        return AppointmentDocViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    override fun onBindViewHolder(holder: AppointmentDocViewHolder, position: Int) {

        val appointment:Appointment=appointmentList[position]
        holder.patName.text=appointment.patName
        holder.aptTime.text = "Time: ${appointment.time}" // Corrected line
        holder.aptDate.text = "Date: ${appointment.appointmentDate}" // Corrected
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, appointment)
        }

    }


}