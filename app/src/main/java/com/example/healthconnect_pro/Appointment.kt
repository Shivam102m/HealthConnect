package com.example.healthconnect_pro
import java.io.Serializable

data class Appointment(
    val appointmentDate: String?=null,
    val docName: String?=null,
    val docUserId: String?=null,
    val patName: String?=null,
    val patUserId: String?=null,
    val time: String?=null,
    val prescription: String?=null,
    val docSpecialization: String?=null,
    val apptID: String?=null
):Serializable
