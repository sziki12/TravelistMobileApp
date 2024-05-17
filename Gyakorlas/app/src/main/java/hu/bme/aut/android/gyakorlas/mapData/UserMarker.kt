package hu.bme.aut.android.gyakorlas.mapData

import kotlinx.serialization.Serializable

@Serializable
data class UserMarker(var username: String?, var latitude: Double, var longitude: Double, var message: String)