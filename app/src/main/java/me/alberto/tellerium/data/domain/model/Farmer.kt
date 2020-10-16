package me.alberto.tellerium.data.domain.model

import java.util.*


data class Farmer(
    val id: UUID = UUID.randomUUID(),
    val imageUrl: String,
    val name: String,
    val gender: String,
    val address: String,
    val dob: String
)