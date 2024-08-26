package com.example.healthconnect_pro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView

class DoctorDashboard : Fragment(R.layout.fragment_doctor_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appointments:CardView=view.findViewById(R.id.take_to_appointments)
        val schedule:CardView=view.findViewById(R.id.take_to_schedule)

        appointments.setOnClickListener {
            val intent=Intent(requireContext(),DocViewAppointments::class.java)
            startActivity(intent)
        }

        schedule.setOnClickListener {
            Toast.makeText(requireContext(),"Failed to access Realtime Database",Toast.LENGTH_LONG).show()
        }

    }

}