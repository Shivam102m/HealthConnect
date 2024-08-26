package com.example.healthconnect_pro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoctorSide : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    private lateinit var db: FirebaseFirestore
    private lateinit var docImage: ImageView
    private lateinit var specialization:String
    private lateinit var userID:String
    private lateinit var tvname:TextView
    private lateinit var tvspec:TextView
    private lateinit var tvemail:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_side)

        val docDash=DoctorDashboard()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flDocDashboard, docDash)
            commit()
        }

        drawerLayout = findViewById(R.id.docDrawer_layout)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val openDrawerButton = findViewById<ImageButton>(R.id.btnMnuDocdash)
        openDrawerButton.setOnClickListener {
            // Open the drawer
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navView = findViewById<NavigationView>(R.id.navViewDoc)
        val headerView = navView.getHeaderView(0) // Get the first header view
        tvname = headerView.findViewById<TextView>(R.id.docNameInMenu)
        tvspec = headerView.findViewById<TextView>(R.id.docSpecInMenu)
        tvemail = headerView.findViewById<TextView>(R.id.docEmailInMenu)
        docImage = headerView.findViewById<ImageView>(R.id.docImageInMenu)


        navView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation view item clicks here.
            when (menuItem.itemId) {
                R.id.docProfile -> {
                }
                R.id.docFeedback -> {
                    Toast.makeText(this, "This feature is under development", Toast.LENGTH_SHORT).show()
                }
                R.id.docAboutUs ->{
                    val intent = Intent(this, AboutUs::class.java)
                    startActivity(intent)
                }
                R.id.docLogOut ->{

                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        Log.d("Firestore initiated","Firestore query started")
        db = FirebaseFirestore.getInstance()
        userID= FirebaseAuth.getInstance().currentUser!!.uid


        CoroutineScope(Dispatchers.Main).launch {
            fetchSpecialization()
        }

    }


    private fun fetchSpecialization() {

        val sRef=db.collection("Specialization").document(userID)
        Log.d("Fetch Specialization","Specilaization retrival started")
        sRef.get().addOnSuccessListener {
            Log.d("Fetch Specialization","Specilaization retrival successful")

            if(it!=null){
                val spec=it.data?.get("specialization")?.toString()
                val name=it.data?.get("name")?.toString()
                val email = it.data?.get("email")?.toString()
                val url=it.data?.get("imageUrl")?.toString()

                Log.d("Fetched Data", "Specialization: $spec")
                Log.d("Fetched Data", "Name: $name")
                Log.d("Fetched Data", "Email: $email")
                Log.d("Fetched Data", "URL: $url")
                Log.d("Specialization Binding","Specilaization Binding started")


                if (spec != null) {
                    specialization=spec
                    tvspec.text=specialization
                    tvname.text=name
                    tvemail.text=email
                    Glide.with(this@DoctorSide).load(url).into(docImage)
                    Log.d("Specialization Binding","Specilaization Binding successful")

                }

            }
        }.addOnFailureListener {
            Log.e("FirestoreError", "Error fetching specialization details", it)
        }
    }


}