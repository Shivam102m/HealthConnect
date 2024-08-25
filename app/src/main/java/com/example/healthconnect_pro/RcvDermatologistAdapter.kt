package com.example.healthconnect_pro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RcvDermatologistAdapter (private val dermatologistList:ArrayList<DoctorData>, private val context: Context, private val listener: OnItemClickListener):RecyclerView.Adapter<RcvDermatologistAdapter.DermatologistViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, doctor: DoctorData)
    }

    class DermatologistViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val docImage: ImageView =itemview.findViewById(R.id.docImgInItem)
        val docName: TextView =itemview.findViewById(R.id.docNameInItem)
        val docAdd: TextView =itemview.findViewById(R.id.docAddInItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DermatologistViewHolder {
        val itemview=
            LayoutInflater.from(parent.context).inflate(R.layout.rv_doc_list_item,parent,false)
        return DermatologistViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return dermatologistList.size
    }

    override fun onBindViewHolder(holder: DermatologistViewHolder, position: Int) {
        val doctor:DoctorData=dermatologistList[position]
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, doctor)
        }
        holder.docName.text=doctor.name
        holder.docAdd.text=doctor.address
        Glide.with(context).load(doctor.imageUrl).into(holder.docImage)    }

}