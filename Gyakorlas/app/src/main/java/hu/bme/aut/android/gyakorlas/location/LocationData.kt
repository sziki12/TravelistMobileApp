package hu.bme.aut.android.gyakorlas.location

import android.app.Activity
import android.location.Location
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.mapData.UserMarker

class LocationData {
    companion object {
        val userMarkers = ArrayList<UserMarker>()

        fun initUsers() {
            val usermarker1 = UserMarker("username1", 47.4742, 19.0592, "")

            val usermarker2 = UserMarker("username2", 47.4727, 19.0577, "Help me")

            val usermarker3 = UserMarker("username3", 47.4744, 19.0635, "I need help")

            val usermarker4 = UserMarker("username4", 47.4774, 19.0460, "")

            val usermarker5 = UserMarker("username5", 37.4226, -122.0837, "Find me!!!")

            val usermarker6 = UserMarker("username6", 37.4220, -122.0828, "Find me!!!")

            val usermarker7 = LocationService.currentLocation?.let { UserMarker("nagyerno", it.latitude, LocationService.currentLocation!!.longitude, "") }

            userMarkers.add(usermarker1)
            userMarkers.add(usermarker2)
            userMarkers.add(usermarker3)
            userMarkers.add(usermarker4)
            userMarkers.add(usermarker5)
            userMarkers.add(usermarker6)
            if (usermarker7 != null) {
                userMarkers.add(usermarker7)
            }
        }
    }
}
