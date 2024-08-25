package com.example.healthconnect_pro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RcvDentistAdapter(private val dentistList:ArrayList<DoctorData>, private val context:Context, private val listener:OnItemClickListener):RecyclerView.Adapter<RcvDentistAdapter.DentistViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, doctor: DoctorData)
    }


    class DentistViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val docImage:ImageView=itemview.findViewById(R.id.docImgInItem)
        val docName:TextView=itemview.findViewById(R.id.docNameInItem)
        val docAdd:TextView=itemview.findViewById(R.id.docAddInItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DentistViewHolder {
        val itemview=
            LayoutInflater.from(parent.context).inflate(R.layout.rv_doc_list_item,parent,false)
        return DentistViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return dentistList.size
    }

    override fun onBindViewHolder(holder: DentistViewHolder, position: Int) {
        val doctor:DoctorData=dentistList[position]
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, doctor)
        }
        holder.docName.text=doctor.name
        holder.docAdd.text=doctor.address
        Glide.with(context).load(doctor.imageUrl).into(holder.docImage)
    }
}