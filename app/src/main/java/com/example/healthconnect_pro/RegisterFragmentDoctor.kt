package com.example.healthconnect_pro

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterFragmentDoctor : Fragment(R.layout.fragment_register_doctor) {

    private lateinit var name: TextInputEditText
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var experience: TextInputEditText
    private lateinit var address: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressbar3: ProgressBar
    private lateinit var db: FirebaseFirestore
    private lateinit  var uri: Uri
    private lateinit var image: ImageView
    private lateinit var btnUpload: Button
    private lateinit var storageRef: FirebaseStorage
    private lateinit var docSpecialization:String
    private lateinit var spinner: Spinner


    val specialization= listOf<String>("Dentist","General Physician","Dermatologist","Pediatrician")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        btnLogin=view.findViewById(R.id.btnGoToLoginDoctor)
        btnRegister=view.findViewById(R.id.btnDoctorCreateAccount)
        name=view.findViewById(R.id.tietDoctorRegisterName)
        username=view.findViewById(R.id.tietDoctorRegisterUsername)
        password=view.findViewById(R.id.tietDoctorRegisterPassword)
        confirmPassword=view.findViewById(R.id.tietDoctorRegisterPasswordConfirm)
        experience=view.findViewById(R.id.tietDoctorRegisterExperience)
        address=view.findViewById(R.id.tietDoctorAddress)
        progressbar3=view.findViewById(R.id.progressBar3)
        progressbar3.visibility=View.INVISIBLE
        db = FirebaseFirestore.getInstance()
        image=view.findViewById(R.id.ivDocRegisterImage)
        btnUpload=view.findViewById(R.id.btnViewImageforDocReg)
        storageRef= FirebaseStorage.getInstance()
        val adjust=view.findViewById<Button>(R.id.justForAdjust)
        adjust.visibility=View.INVISIBLE


        spinner=view.findViewById(R.id.spinner)
        val arrayAdapter=ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,specialization)
        spinner.adapter=arrayAdapter
        spinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                docSpecialization=specialization[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val galleryImage=registerForActivityResult(
            ActivityResultContracts.GetContent(),
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
        val password = password.text.toString().trim() + "doc"
        val confirmPassword = confirmPassword.text.toString().trim() + "doc"
        if (confirmPassword == password) {
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                progressbar3.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.createUserWithEmailAndPassword(email, password).await()
                        // Call saveData() and wait for it to complete before proceeding
                        saveData()
                        withContext(Dispatchers.Main) {
                            auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(requireContext(), "Please verify your email", Toast.LENGTH_LONG).show()
                                requireActivity().onBackPressed()
                            }
                            // Ensure the progress bar is made invisible after successful registration
                            progressbar3.visibility = View.INVISIBLE
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                            // Ensure the progress bar is made invisible in case of an exception
                            progressbar3.visibility = View.INVISIBLE
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
        val storageRef = storageRef.getReference("DoctorImages").child(System.currentTimeMillis().toString())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("URI", "URI: $uri")
                Log.d("Specialization","Specialization recorded is $docSpecialization")
                val uploadTask = storageRef.putFile(uri).await()
                val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await()
                Log.d("URI", "URI from firestore: $downloadUrl")
                if (downloadUrl != null) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    Log.d("Save Data Function", "UserId: $userId")
                    val sname=name.text.toString().trim()
                    val semail = username.text.toString().trim()
                    val address=address.text.toString().trim()
                    val experience=experience.text.toString()
                    val special=docSpecialization
                    val docMap= hashMapOf(
                        "name" to sname,
                        "email" to semail,
                        "imageUrl" to downloadUrl.toString(),
                        "address" to address,
                        "experience" to experience,
                        "specialization" to special,
                        "userIdDoc" to userId
                    )
                    val specMap= hashMapOf(
                        "specialization" to special,
                        "name" to sname,
                        "email" to semail,
                        "imageUrl" to downloadUrl.toString()
                    )
                    Log.d("Save Data Function", "name: $sname")
                    Log.d("Save Data Function", "email: $semail")
                    Log.d("Save Data Function", "imageUrl: ${downloadUrl.toString()}")
                    Log.d("Save Data Function", "address: $address")
                    Log.d("Save Data Function", "experience: $experience")
                    Log.d("Save Data Function", "specialization: $special")
                    Log.d("FireStore access:","Accessing started")
                    db.collection(special).document(userId).set(docMap).addOnSuccessListener {
                        Log.d("Saving Success","docSpecialization Saved Successfully")
                        db.collection("Specialization").document(userId).set(specMap).addOnSuccessListener {
                            Log.d("Saving Success","Specialization Saved Successfully")

                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
