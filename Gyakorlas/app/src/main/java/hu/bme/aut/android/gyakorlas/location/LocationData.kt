package hu.bme.aut.android.gyakorlas.location

import android.app.Activity
import android.location.Location
import hu.bme.aut.android.gyakorlas.mapData.MapMarker

class LocationData {
    companion object {
        val userLocations = ArrayList<Location>()

        fun initUsers() {
            val location1 = Location("Provider1")
            location1.latitude = 47.12345
            location1.longitude = 19.67890

            val location2 = Location("Provider2")
            location2.latitude = 48.54321
            location2.longitude = 18.98765

            val location3 = Location("Provider3")
            location3.latitude = 47.13345
            location3.longitude = 19.66890

            val location4 = Location("Provider4")
            location3.latitude = 48.8566
            location3.longitude = 2.3522

            userLocations.add(location1)
            userLocations.add(location2)
            userLocations.add(location3)
            userLocations.add(location4)
        }
    }

    fun addLocation(location: Location) {
        userLocations.add(location)
    }

    fun getLocations(): ArrayList<Location> {
        return userLocations
    }
}
