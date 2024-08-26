package com.example.healthconnect_pro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView


class PatientDashboard : Fragment(R.layout.fragment_patient_dashboard) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectDoctor=view.findViewById<CardView>(R.id.goToSelectDoctor)
        val viewAppointment=view.findViewById<CardView>(R.id.goToPatViewAppt)
        val askQuestions=view.findViewById<CardView>(R.id.goToAskAwkwardQues)


        selectDoctor.setOnClickListener {
            val intent = Intent(requireContext(), SelectDoctor::class.java)
            startActivity(intent)
        }

        viewAppointment.setOnClickListener {
            val intent = Intent(requireContext(), PatViewAppointments::class.java)
            startActivity(intent)
        }

        askQuestions.setOnClickListener(){
            Toast.makeText(requireContext(),"This feature is under development",Toast.LENGTH_LONG).show()
        }

    }

}