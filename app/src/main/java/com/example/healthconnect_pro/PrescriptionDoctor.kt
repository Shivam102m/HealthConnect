package com.example.healthconnect_pro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class PrescriptionDoctor : AppCompatActivity() {


    private lateinit var doctorName:String
    private lateinit var doctorSpec:String
    private  var date:String?=null
    private lateinit var docID:String
    private lateinit var patName:String
    private lateinit var time:String
    private lateinit var patID:String
    private  var prescription:String?=null
    private lateinit var apptID:String
    private var rating=0
    private lateinit var ivPatImage: ImageView
    private lateinit var tvPatName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvprescription: EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var patUri:String
    private lateinit var sPatappend:String
    private lateinit var sDocappend:String
    private lateinit var sPatappt:String
    private lateinit var sDocappt:String
    private lateinit var newPres:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_doctor)

        doctorName= intent.getStringExtra("doctorName").toString()
        doctorSpec= intent.getStringExtra("doctorSpec").toString()
        date=intent.getStringExtra("date")
        docID= intent.getStringExtra("docID").toString()
        patName= intent.getStringExtra("patName").toString()
        patID=intent.getStringExtra("patID").toString()
        prescription=intent.getStringExtra("prescription")
        apptID=intent.getStringExtra("apptID").toString()
        rating=intent.getIntExtra("rating",0)
        time= intent.getStringExtra("time").toString()
        val back=findViewById<ImageButton>(R.id.btnbackPrescriptionDoc)

        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }


        //
        db= FirebaseFirestore.getInstance()

        sPatappend= patName?.split(" ")?.get(0).toString()
        sDocappend= doctorName?.split(" ")?.get(0).toString()

        sDocappt=docID+sDocappend

        val dref=db.collection(sDocappt).document(apptID)

        dref.get().addOnSuccessListener {
            if (it!=null){
                patID = it.data?.get("patUserID")?.toString().toString()

            }
        }.addOnSuccessListener {
            sPatappt=patID+sPatappend
        }


        ivPatImage=findViewById(R.id.patImgInPrescription)
        tvPatName=findViewById(R.id.patNameInPrescription)
        tvDate=findViewById(R.id.dateInPrescriptionDoc)
        tvprescription=findViewById(R.id.actualPrescriptionDoc)

        tvDate.text=date
        tvPatName.text=patName

        val ref=db.collection("Patient").document(patID)
        ref.get().addOnSuccessListener {
            if (it != null) {
                patUri = it.data?.get("url")?.toString().toString()
            }
        }.addOnSuccessListener {
            Glide.with(this)
                .load(patUri)
                .error(R.drawable.patient)
                .placeholder(R.drawable.patient)
                .into(ivPatImage)
        }.addOnFailureListener { exception ->
            Log.e("Firestore Error", "Error getting document", exception)
        }
        tvprescription.text= Editable.Factory.getInstance().newEditable(prescription)


        val update=findViewById<Button>(R.id.btnUpdatePrescription)


        update.setOnClickListener(){
            newPres= tvprescription.text.toString()
            updatePres()
        }

    }

    private fun updatePres() {
        Log.d("Update Prescription","updatePres Started")

        Log.d("Update Prescription","docid: $docID")
        Log.d("Update Prescription","patid: $patID")

        val appointmentMap= mapOf(
            "docUserId" to docID,
            "patUserID" to patID,
            "docName" to doctorName,
            "patName" to patName,
            "appointmentDate" to date,
            "docSpecialization" to doctorSpec,
            "time" to time,
            "prescription" to newPres,
            "apptID" to apptID
        )

        Log.d("Update Prescription","Update process started")
        db.collection(sPatappt).document(apptID).update(appointmentMap).addOnSuccessListener {

            Log.d("Update Prescription","Phase1 Completed")
            Toast.makeText(this,"Prescription Update successful",Toast.LENGTH_LONG).show()
            db.collection(sDocappt).document(apptID).update(appointmentMap)

            Log.d("Update Prescription","Phase2 Completed")
        }

    }
}