package com.example.healthconnect_pro
import java.io.Serializable

data class DoctorData(
    val imageUrl: String?=null,
    val name: String?=null,
    val specialization: String?=null,
    val userIdDoc: String?=null,
    val rating: Int?=null,
    val address: String?=null,
    val noOfRating: Int=0
):Serializable
