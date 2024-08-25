package com.example.healthconnect_pro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RcvGeneralPhysicianAdapter (private val generalPhysicianList:ArrayList<DoctorData>, private val context: Context, private val listener: OnItemClickListener):RecyclerView.Adapter<RcvGeneralPhysicianAdapter.GeneralPhysicianHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, doctor: DoctorData)
    }

    class GeneralPhysicianHolder(itemview: View) :RecyclerView.ViewHolder(itemview){
        val docImage: ImageView =itemview.findViewById(R.id.docImgInItem)
        val docName: TextView =itemview.findViewById(R.id.docNameInItem)
        val docAdd: TextView =itemview.findViewById(R.id.docAddInItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralPhysicianHolder {
        val itemview=
            LayoutInflater.from(parent.context).inflate(R.layout.rv_doc_list_item,parent,false)
        return RcvGeneralPhysicianAdapter.GeneralPhysicianHolder(itemview)
    }

    override fun getItemCount(): Int {
        return generalPhysicianList.size
    }

    override fun onBindViewHolder(holder: GeneralPhysicianHolder, position: Int) {
        val doctor:DoctorData=generalPhysicianList[position]
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, doctor)
        }
        holder.docName.text=doctor.name
        holder.docAdd.text=doctor.address
        Glide.with(context).load(doctor.imageUrl).into(holder.docImage)
    }


}