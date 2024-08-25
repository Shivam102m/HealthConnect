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

class ViewGeneralPhysician : Fragment(R.layout.fragment_view_general_physician),RcvGeneralPhysicianAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var generalPhysicianlist:ArrayList<DoctorData>
    private lateinit var generalPhysicianAdapter: RcvGeneralPhysicianAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = view?.findViewById(R.id.rcvGeneralPhysician) ?: throw IllegalStateException("rcvDentist view not found")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generalPhysicianlist= arrayListOf()

        generalPhysicianAdapter= RcvGeneralPhysicianAdapter(generalPhysicianlist,requireContext(),this)
        recyclerView.adapter=generalPhysicianAdapter

        EventChangeListener()


    }

    private fun EventChangeListener() {
        db= FirebaseFirestore.getInstance()
        db.collection("General Physician").addSnapshotListener(MetadataChanges.EXCLUDE ){ snapshot, error->

            if(error!=null){

            }
            if(snapshot!=null){
                for (doc: DocumentChange in snapshot?.documentChanges!!) {

                    if (doc.type== DocumentChange.Type.ADDED){
                        generalPhysicianlist.add(doc.document.toObject(DoctorData::class.java))
                    }
                }

                generalPhysicianAdapter.notifyDataSetChanged()
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