package com.example.healthconnect_pro

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragmentPatient : Fragment(R.layout.fragment_login_patient) {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginEmail: TextInputEditText
    private lateinit var loginPassword: TextInputEditText
    private lateinit var progress:ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val btnRegister=view.findViewById<Button>(R.id.btnPatientGoToRegister)
        val btnLogin=view.findViewById<Button>(R.id.btnPatientlgn)
        loginEmail=view.findViewById(R.id.tietPatientUsername)
        loginPassword=view.findViewById(R.id.tietPatientPassword)
        val patientRegisterFragment=RegisterFragmentPatient()
        progress=view.findViewById(R.id.progressBar)
        progress.visibility=View.INVISIBLE
        btnRegister.setOnClickListener(){
            (activity as MainActivity).changeFragment(patientRegisterFragment)

        }
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Make the progress bar visible only when the login attempt is made
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    val verify = auth.currentUser?.isEmailVerified
                    if (verify == true) {
                        withContext(Dispatchers.Main) {
                            checkLoggedInState()
                            // Ensure the progress bar is made invisible after successful login
                            progress.visibility = View.INVISIBLE
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Please verify your email", Toast.LENGTH_LONG).show()
                            // Ensure the progress bar is made invisible if email verification is required
                            progress.visibility = View.INVISIBLE
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                        // Ensure the progress bar is made invisible in case of an exception
                        progress.visibility = View.INVISIBLE
                    }
                }
            }
        } else {
            // Optionally, show a message if the fields are empty
            Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(){
        val intent = Intent(requireContext(), PatientSide::class.java)
        startActivity(intent)
    }
    private fun checkLoggedInState() {
        if (auth.currentUser == null) { // not logged in
            Looper.prepare()
            Toast.makeText(requireContext(), "Some error occurred please retry whatever you were doing", Toast.LENGTH_LONG).show()
            Looper.loop()

        } else {
            updateUI()
        }
    }
}