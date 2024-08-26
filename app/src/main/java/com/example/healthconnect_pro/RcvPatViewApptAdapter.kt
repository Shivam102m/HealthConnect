package com.example.healthconnect_pro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RcvPatViewApptAdapter(private val appointmentList:ArrayList<Appointment>, private val context: Context, private val listener: OnItemClickListener):RecyclerView.Adapter<RcvPatViewApptAdapter.AppointmentViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, appointment: Appointment)
    }

    class AppointmentViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val docName:TextView=itemview.findViewById(R.id.docNameInViewAppt)
        val docSpec:TextView=itemview.findViewById(R.id.docSpecInViewAppt)
        val aptTime:TextView=itemview.findViewById(R.id.ViewApptTime)
        val aptDate:TextView=itemview.findViewById(R.id.ViewApptDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val itemview=
            LayoutInflater.from(parent.context).inflate(R.layout.rv_appointment_list,parent,false)
        return AppointmentViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {

        val appointment:Appointment=appointmentList[position]
        holder.docName.text=appointment.docName
        holder.docSpec.text=appointment.docSpecialization
        holder.aptTime.text = "Time: ${appointment.time}" // Corrected line
        holder.aptDate.text = "Date: ${appointment.appointmentDate}" // Corrected
        holder.itemView.setOnClickListener {
        listener.onItemClick(position, appointment)
    }
    }

}