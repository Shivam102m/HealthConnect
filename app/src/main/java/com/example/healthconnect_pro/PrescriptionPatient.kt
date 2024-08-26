package com.example.healthconnect_pro

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import kotlin.properties.Delegates

class PrescriptionPatient : AppCompatActivity() {

    private lateinit var doctorName:String
    private lateinit var doctorSpec:String
    private  var date:String?=null
    private lateinit var docID:String
    private lateinit var patName:String
    private  var patID:String?=null
    private  var prescription:String?=null
    private lateinit var apptID:String
    private var rating=0
    private lateinit var ivDocImage:ImageView
    private lateinit var tvDocName:TextView
    private lateinit var tvDocSpec:TextView
    private lateinit var tvDate:TextView
    private lateinit var tvprescription:TextView
    private lateinit var db:FirebaseFirestore
    private lateinit var docUri:String
    private lateinit var sPatappend:String
    private lateinit var sDocappend:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_patient)

        doctorName= intent.getStringExtra("doctorName").toString()
        doctorSpec= intent.getStringExtra("doctorSpec").toString()
        date=intent.getStringExtra("date")
        docID= intent.getStringExtra("docID").toString()
        patName= intent.getStringExtra("patName").toString()
        patID=intent.getStringExtra("patID")
        prescription=intent.getStringExtra("prescription")
        apptID=intent.getStringExtra("apptID").toString()
        rating=intent.getIntExtra("rating",0)
        val back=findViewById<ImageButton>(R.id.btnbackPrescription)

        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }


         sPatappend= patName?.split(" ")?.get(0).toString()
         sDocappend= doctorName?.split(" ")?.get(0).toString()

        ivDocImage=findViewById(R.id.docImgInPrescription)
        tvDocName=findViewById(R.id.docNameInPrescription)
        tvDocSpec=findViewById(R.id.docSpecInPrescription)
        tvDate=findViewById(R.id.dateInPrescription)
        tvprescription=findViewById(R.id.actualPrescription)


        tvDocName.text=doctorName
        tvDocSpec.text=doctorSpec
        tvDate.text=date


        db=FirebaseFirestore.getInstance()
        Log.d("Firestore Query", "Attempting to retrieve document")
        val ref = db.collection(doctorSpec).document(docID)
        ref.get().addOnSuccessListener {
            if (it != null) {
                docUri = it.data?.get("imageUrl")?.toString().toString()
            }
        }.addOnSuccessListener {
            Log.d("URI message", "The retrieved uri is $docUri")
            Glide.with(this)
                .load(docUri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(ivDocImage)
        }.addOnFailureListener { exception ->
            Log.e("Firestore Error", "Error getting document", exception)
        }

        tvprescription.text=prescription


    }



}