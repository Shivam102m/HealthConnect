package com.example.healthconnect_pro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges


class ViewPediatrician : Fragment(R.layout.fragment_view_pediatrician),RcvPediatricianAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pediatricianList:ArrayList<DoctorData>
    private lateinit var pediatricianAdapter: RcvPediatricianAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = view?.findViewById(R.id.rcvPediatrician) ?: throw IllegalStateException("rcvPediatrician view not found")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pediatricianList= arrayListOf()

        pediatricianAdapter=RcvPediatricianAdapter(pediatricianList,requireContext(),this)
        recyclerView.adapter=pediatricianAdapter

        EventChangeListener()
    }


    private fun EventChangeListener() {
        db= FirebaseFirestore.getInstance()
        db.collection("Pediatrician").addSnapshotListener(MetadataChanges.EXCLUDE ){ snapshot, error->

            if(error!=null){

            }
            if(snapshot!=null){
                for (doc: DocumentChange in snapshot?.documentChanges!!) {

                    if (doc.type== DocumentChange.Type.ADDED){
                        pediatricianList.add(doc.document.toObject(DoctorData::class.java))
                    }
                }

                pediatricianAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(position: Int, doctor: DoctorData) {
        val intent = Intent(requireContext(), DocAppointmentBooking::class.java)
        intent.putExtra("doctorName", doctor.name)
        intent.putExtra("doctorAddress", doctor.address)
        intent.putExtra("doctorImageUrl", doctor.imageUrl)
        intent.putExtra("doctorSpecialization",doctor.specialization)
        intent.putExtra("doctorUserId",doctor.userIdDoc)
        startActivity(intent)
        requireActivity().finish()
    }

}