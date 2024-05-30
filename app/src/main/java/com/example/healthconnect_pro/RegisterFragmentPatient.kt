package com.example.healthconnect_pro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URI


class RegisterFragmentPatient : Fragment(R.layout.fragment_register_patient) {
    private lateinit var name: TextInputEditText
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressbar2:ProgressBar
    private lateinit var db: FirebaseFirestore
    private lateinit  var uri:Uri
    private lateinit var image:ImageView
    private lateinit var btnUpload:Button
    private lateinit var storageRef:FirebaseStorage


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        btnLogin=view.findViewById(R.id.btnGoToLoginPatient)
        btnRegister=view.findViewById(R.id.btnPatientCreateAccount)
        name=view.findViewById(R.id.tietPatientRegisterName)
        username=view.findViewById(R.id.tietPatientRegisterUsername)
        password=view.findViewById(R.id.tietPatientRegisterPassword)
        confirmPassword=view.findViewById(R.id.tietPatientRegisterPasswordConfirm)
        progressbar2=view.findViewById(R.id.progressBar2)
        progressbar2.visibility=View.INVISIBLE
        db = Firebase.firestore
        image=view.findViewById(R.id.ivPatientRegisterImage)
        btnUpload=view.findViewById(R.id.btnViewImageforPatReg)
        storageRef= FirebaseStorage.getInstance()

        val galleryImage=registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                image.setImageURI(it)
                if (it != null) {
                    uri=it
                }
            })

        btnUpload.setOnClickListener {
            galleryImage.launch("image/*")
        }

        btnRegister.setOnClickListener {
            registerUser()

        }

        btnLogin.setOnClickListener(){
            requireActivity().onBackPressed()
        }

    }

    private fun registerUser() {
        val email = username.text.toString().trim()
        val password = password.text.toString().trim()
        val confirmPassword = confirmPassword.text.toString().trim()
        if (confirmPassword == password) {
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                progressbar2.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.createUserWithEmailAndPassword(email, password).await()
                        withContext(Dispatchers.Main) {
                            saveData()
                            auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(requireContext(), "Please verify your email", Toast.LENGTH_LONG).show()
                                requireActivity().onBackPressed()
                            }
                            // Ensure the progress bar is made invisible after successful registration
                            progressbar2.visibility = View.INVISIBLE
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                            // Ensure the progress bar is made invisible in case of an exception
                            progressbar2.visibility = View.INVISIBLE
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_LONG).show()
        }
    }




    private fun saveData() {
        val storageRef = storageRef.getReference("PatientImages").child(System.currentTimeMillis().toString())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uploadTask = storageRef.putFile(uri).await()
                val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await()
                if (downloadUrl != null) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val sname=name.text.toString().trim()
                    val semail = username.text.toString().trim()
                    val databasereference=FirebaseDatabase.getInstance().getReference("Patient")
                    val patientMap= hashMapOf(
                        "name" to sname,
                        "email" to semail,
                        "url" to downloadUrl.toString()
                    )
                    db.collection("Patient").document(userId).set(patientMap)

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }



}