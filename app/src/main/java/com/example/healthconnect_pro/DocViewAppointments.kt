package com.example.healthconnect_pro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class DocViewAppointments : AppCompatActivity(),RcvDocViewApptAdapter.OnItemClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentlist:ArrayList<Appointment>
    private lateinit var appointmentAdapter: RcvDocViewApptAdapter
    private lateinit var db: FirebaseFirestore
    private var userID: String?=null
    private var docName: String?=null
    private lateinit var collName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_view_appointments)
        val back=findViewById<ImageButton>(R.id.btnbackViewApptDoc)

        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        recyclerView=findViewById(R.id.rcvAppointmentDoc)
        recyclerView.layoutManager= LinearLayoutManager(this)

        appointmentlist= arrayListOf()
        appointmentAdapter= RcvDocViewApptAdapter(appointmentlist,this,this)

        recyclerView.adapter=appointmentAdapter
        db = FirebaseFirestore.getInstance()
        userID= FirebaseAuth.getInstance().currentUser!!.uid
        val ref=db.collection("Specialization").document(userID!!)
        ref.get().addOnSuccessListener {
            if(it!=null){
                docName=it.data?.get("name")?.toString()
                collName=userID+docName?.split(" ")?.get(0)
                EventChangeListener()
            }
        }

    }

    private fun EventChangeListener() {
        db=FirebaseFirestore.getInstance()
        db.collection(collName).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("FireStore Error of view Appointment", error.message.toString())
                    return
                }

                for (dc: DocumentChange in value?.documentChanges!!) {

                    if (dc.type== DocumentChange.Type.ADDED){
                        appointmentlist.add(dc.document.toObject(Appointment::class.java))
                    }

                }

                appointmentAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClick(position: Int, appointment: Appointment) {
        val intent= Intent(this,PrescriptionDoctor::class.java)
        intent.putExtra("doctorName",appointment.docName)
        intent.putExtra("doctorSpec",appointment.docSpecialization)
        intent.putExtra("date",appointment.appointmentDate)
        intent.putExtra("docID",appointment.docUserId)
        intent.putExtra("patName",appointment.patName)
        intent.putExtra("patID",appointment.patUserId)
        intent.putExtra("prescription",appointment.prescription)
        intent.putExtra("apptID",appointment.apptID)
        intent.putExtra("time",appointment.time)
        startActivity(intent)    }


}