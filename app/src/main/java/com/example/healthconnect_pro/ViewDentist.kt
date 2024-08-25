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
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import java.util.concurrent.Executors


class ViewDentist : Fragment(R.layout.fragment_view_dentist),RcvDentistAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dentistlist:ArrayList<DoctorData>
    private lateinit var dentistAdapter: RcvDentistAdapter
    private lateinit var db:FirebaseFirestore
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = view?.findViewById(R.id.rcvDentist) ?: throw IllegalStateException("rcvDentist view not found")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dentistlist= arrayListOf()

        dentistAdapter= RcvDentistAdapter(dentistlist,requireContext(),this)
        recyclerView.adapter=dentistAdapter

        EventChangeListener()


    }

    private fun EventChangeListener() {
        db= FirebaseFirestore.getInstance()
        db.collection("Dentist").addSnapshotListener(MetadataChanges.EXCLUDE ){ snapshot,error->

            if(error!=null){

            }
            if(snapshot!=null){
                for (doc:DocumentChange in snapshot?.documentChanges!!) {

                    if (doc.type==DocumentChange.Type.ADDED){
                        dentistlist.add(doc.document.toObject(DoctorData::class.java))
                    }
                }

                dentistAdapter.notifyDataSetChanged()
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