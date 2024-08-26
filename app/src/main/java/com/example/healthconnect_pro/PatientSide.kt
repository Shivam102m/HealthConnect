package com.example.healthconnect_pro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PatientSide : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    private lateinit var db:FirebaseFirestore
    private lateinit var patImage:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_side)
        val propDash=PatientDashboard()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flPatientDashboard, propDash)
            commit()
        }

        drawerLayout = findViewById(R.id.myDrawer_layout)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val openDrawerButton = findViewById<ImageButton>(R.id.btnmnupatdash)
        openDrawerButton.setOnClickListener {
            // Open the drawer
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navView = findViewById<NavigationView>(R.id.navViewPat)
        val headerView = navView.getHeaderView(0) // Get the first header view
        val tvname = headerView.findViewById<TextView>(R.id.patientNameInMenu)
        val tvemail = headerView.findViewById<TextView>(R.id.patientEmailInMenu)
        patImage = headerView.findViewById<ImageView>(R.id.patImage)

        // Set up the NavigationView's item selected listener
        navView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation view item clicks here.
            when (menuItem.itemId) {
                R.id.patProfile -> {
                }
                R.id.patMedicalRecords -> {
                    Toast.makeText(this, "This feature is under development", Toast.LENGTH_SHORT).show()
                }
                R.id.patSettings -> {
                    Toast.makeText(this, "This feature is under development", Toast.LENGTH_SHORT).show()
                }
                R.id.patAboutUs ->{
                    val intent = Intent(this, AboutUs::class.java)
                    startActivity(intent)
                }
                R.id.patLogOut ->{

                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        db = FirebaseFirestore.getInstance()
        val userID=FirebaseAuth.getInstance().currentUser!!.uid
        val ref=db.collection("Patient").document(userID)

        // Start from here for new functions
        ref.get().addOnSuccessListener {
            if(it!=null){
                val name=it.data?.get("name")?.toString()
                val email = it.data?.get("email")?.toString()
                val url=it.data?.get("url")?.toString()

                tvname.text=name
                tvemail.text=email
                Glide.with(this).load(url).into(patImage)
            }
        }




    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Check if there is more than one fragment in the back stack
            if (supportFragmentManager.backStackEntryCount > 1) {
                // Pop the current fragment from the back stack
                supportFragmentManager.popBackStack()
            } else {
                // If not, handle the back press as usual
                super.onBackPressed()
            }
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flPatientDashboard, fragment)
            .addToBackStack(null)
            .commit()
    }
}