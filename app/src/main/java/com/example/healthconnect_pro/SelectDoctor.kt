package com.example.healthconnect_pro

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.addCallback



class SelectDoctor : AppCompatActivity() {
    private lateinit var btnptsideDentist: Button
    private lateinit var btnptsideGeneral: Button
    private lateinit var btnptsideGyno: Button
    private lateinit var btnptsideNeuro: Button
    private lateinit var btnback:ImageButton
    private var lastClickedButton: Button? = null
    private val dent=ViewDentist()
    private val genPhy=ViewGeneralPhysician()
    private val derma=ViewDermatologist()
    private val pedia=ViewPediatrician()

    private val buttonClickListener = View.OnClickListener { view ->
        val clickedButton = view as Button
        // Reset the background color of the last clicked button
        lastClickedButton?.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8DFDF"))
        // Change the background color of the clicked button
        clickedButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFC107")) // Example color
        // Update the last clicked button reference
        lastClickedButton = clickedButton

        // Additional functionality based on which button was clicked
        when (clickedButton.id) {
            R.id.btnptsideDentist -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flPatViewDoc,dent)
                    commit()
                }
            }
            R.id.btnptsideGeneral -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flPatViewDoc,genPhy)
                    commit()
                }
            }
            R.id.btnptsideGyno -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flPatViewDoc,pedia)
                    commit()
                }
            }
            R.id.btnptsideNeuro -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flPatViewDoc,derma)
                    commit()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_doctor)

        btnptsideDentist = findViewById(R.id.btnptsideDentist)
        btnptsideGeneral = findViewById(R.id.btnptsideGeneral)
        btnptsideGyno = findViewById(R.id.btnptsideGyno)
        btnptsideNeuro = findViewById(R.id.btnptsideNeuro)

        btnptsideDentist.setOnClickListener(buttonClickListener)
        btnptsideGeneral.setOnClickListener(buttonClickListener)
        btnptsideGyno.setOnClickListener(buttonClickListener)
        btnptsideNeuro.setOnClickListener(buttonClickListener)

        btnptsideDentist.performClick()
        btnback=findViewById(R.id.btntakeback)

        btnback.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }




    }


}