package com.example.healthconnect_pro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class DirectingFragment : Fragment(R.layout.fragment_directing) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doctorLoginFragment=LoginFragmentDoctor()
        val patientLoginFragment=LoginFragmentPatient()
        val doc=view.findViewById<ImageButton>(R.id.imgDirectingDoctor)
        val pat=view.findViewById<ImageButton>(R.id.imgDirectingPatient)

        doc.setOnClickListener(){
            (activity as MainActivity).changeFragment(doctorLoginFragment)
        }
        pat.setOnClickListener(){
            (activity as MainActivity).changeFragment(patientLoginFragment)
        }


    }
}