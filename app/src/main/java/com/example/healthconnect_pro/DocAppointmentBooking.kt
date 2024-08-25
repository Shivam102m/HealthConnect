package com.example.healthconnect_pro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class DocAppointmentBooking : AppCompatActivity() {

    private lateinit var dateContainer: LinearLayout
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    lateinit var tpText:TextView
    private lateinit var btn10am: Button
    private lateinit var btn11am: Button
    private lateinit var btn12pm: Button
    private lateinit var btn01pm: Button
    private lateinit var btn03pm: Button
    private lateinit var btn04pm: Button
    private lateinit var btn05pm: Button
    private lateinit var btn06pm: Button
    private lateinit var btn07pm: Button
    private lateinit var btnBookAppointment: Button
    private  var doctorName: String?=null
    private  var doctorUserId: String?=null
    private var doctorSpecialization: String?=null
    private lateinit var db: FirebaseFirestore
    private lateinit var userId:String
    private  var patname:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_appointment_booking)

        doctorName = intent.getStringExtra("doctorName")
        val doctorAddress = intent.getStringExtra("doctorAddress")
        val doctorImageUrl = intent.getStringExtra("doctorImageUrl")
        doctorSpecialization = intent.getStringExtra("doctorSpecialization")
        doctorUserId = intent.getStringExtra("doctorUserId")
//        Log.d("DocAppointmentBooking", "docUserId by intent: $doctorUserId")

        val name:TextView=findViewById(R.id.docNameInBookApt)
        val spec:TextView=findViewById(R.id.docSpecInBookApt)
        val address:TextView=findViewById(R.id.docAddInBookApt)
        val img:ImageView=findViewById(R.id.docImgInappt)
        tpText=findViewById(R.id.tp)
         btn10am = findViewById(R.id.btn10am)
         btn11am= findViewById(R.id.btn11am)
         btn12pm= findViewById(R.id.btn12pm)
         btn01pm = findViewById(R.id.btn1pm)
         btn03pm = findViewById(R.id.btn3pm)
         btn04pm = findViewById(R.id.btn4pm)
         btn05pm = findViewById(R.id.btn5pm)
         btn06pm = findViewById(R.id.btn6pm)
         btn07pm = findViewById(R.id.btn7pm)
        btnBookAppointment=findViewById(R.id.btnBookAppt)


        btn10am.setOnClickListener { onTimeButtonClicked(btn10am) }
        btn11am.setOnClickListener { onTimeButtonClicked(btn11am) }
        btn12pm.setOnClickListener { onTimeButtonClicked(btn12pm) }
        btn01pm.setOnClickListener { onTimeButtonClicked(btn01pm) }
        btn03pm.setOnClickListener { onTimeButtonClicked(btn03pm) }
        btn04pm.setOnClickListener { onTimeButtonClicked(btn04pm) }
        btn05pm.setOnClickListener { onTimeButtonClicked(btn05pm) }
        btn06pm.setOnClickListener { onTimeButtonClicked(btn06pm) }
        btn07pm.setOnClickListener { onTimeButtonClicked(btn07pm) }
        name.text=doctorName
        spec.text=doctorSpecialization
        address.text=doctorAddress
        Glide.with(this).load(doctorImageUrl).into(img)
        db = FirebaseFirestore.getInstance()
        userId= FirebaseAuth.getInstance().currentUser!!.uid
        val ref=db.collection("Patient").document(userId)


        ref.get().addOnSuccessListener {
            if(it!=null){
                patname=it.data?.get("name")?.toString()
            }
        }


        dateContainer = findViewById(R.id.dateContainer)

        // Generate dates from today to five days after today
        val calendar = Calendar.getInstance()
        for (i in 0 until 5) {
            val date = calendar.time
            val dateString = SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)
            addDateButton(dateString)
            calendar.add(Calendar.DATE, 1)
        }
        btnBookAppointment.setOnClickListener(){
            bookAppointment()
        }

        val back=findViewById<ImageView>(R.id.btnback)
        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

    }

    private fun addDateButton(date: String) {
        val button = Button(this)
        button.text = date

        button.setOnClickListener {
            // Reset background color for all buttons
            for (i in 0 until dateContainer.childCount) {
                val child = dateContainer.getChildAt(i) as Button
                child.setBackgroundColor(resources.getColor(R.color.button_color))
            }
            // Change background color of the clicked button
            button.setBackgroundColor(resources.getColor(R.color.selected_date_background))
            // Store the selected date
            selectedDate = date
            tpText.text=selectedDate

        }
        dateContainer.addView(button)
    }

    private fun onTimeButtonClicked(button: Button) {
        // Reset all buttons to default state
        resetButtonStates()

        // Change the clicked button's color
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_button_color))

        // Copy the text of the clicked button to the variable
        selectedTime = button.text.toString()
    }

    private fun resetButtonStates() {

        btn10am.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn11am.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn12pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn01pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn03pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn04pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn05pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn06pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))
        btn07pm.setBackgroundColor(ContextCompat.getColor(this, R.color.default_button_color))

    }

    private fun bookAppointment(){
        val sDocName=doctorName
        val sDocID=doctorUserId
        var sPatName:String?=patname
        val timeSlot=selectedTime
        val date=selectedDate
        val prescription="No Prescription yet"
        var sPatappend= sPatName?.split(" ")?.get(0)
        var sDocappend= sDocName?.split(" ")?.get(0)
//        Log.d("DocAppointmentBooking", "docUserId by fxn: $sDocID")
//        Log.d("DocAppointmentBooking", "sPatappend: $sPatappend")
//        Log.d("DocAppointmentBooking", "sDocappend: $sDocappend")
        val patAptcoll=userId+sPatappend
        val docAptcoll=sDocID+sDocappend

//        Log.d("DocAppointmentBooking", "PatApptCollection: $patAptcoll")
//        Log.d("DocAppointmentBooking", "DocApptCollection: $docAptcoll")
        val docID=sDocID

        val apptID=getApptId()



//        Log.d("DocAppointmentBooking", "docUserId final: $docID")

        val appointmentMap= hashMapOf(
            "docUserId" to docID,
            "patUserID" to userId,
            "docName" to sDocName,
            "patName" to sPatName,
            "appointmentDate" to date,
            "docSpecialization" to doctorSpecialization,
            "time" to timeSlot,
            "prescription" to prescription,
            "apptID" to apptID
        )

        db.collection(patAptcoll).document(apptID).set(appointmentMap).addOnSuccessListener {
            db.collection(docAptcoll).document(apptID).set(appointmentMap)
            finish()
        }

    }
    private fun getApptId(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..16)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }


}