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


class ViewDermatologist : Fragment(R.layout.fragment_view_dermatologist),RcvDermatologistAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dermatologistlist:ArrayList<DoctorData>
    private lateinit var dermatologistAdapter: RcvDermatologistAdapter
    private lateinit var db: FirebaseFirestore
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = view?.findViewById(R.id.rcvDermatologist) ?: throw IllegalStateException("rcvDentist view not found")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dermatologistlist= arrayListOf()

        dermatologistAdapter= RcvDermatologistAdapter(dermatologistlist,requireContext(),this)
        recyclerView.adapter=dermatologistAdapter

        EventChangeListener()


    }

    private fun EventChangeListener() {
        db= FirebaseFirestore.getInstance()
        db.collection("Dermatologist").addSnapshotListener(MetadataChanges.EXCLUDE ){ snapshot, error->

            if(error!=null){

            }
            if(snapshot!=null){
                for (doc: DocumentChange in snapshot?.documentChanges!!) {

                    if (doc.type== DocumentChange.Type.ADDED){
                        dermatologistlist.add(doc.document.toObject(DoctorData::class.java))
                    }
                }

                dermatologistAdapter.notifyDataSetChanged()
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