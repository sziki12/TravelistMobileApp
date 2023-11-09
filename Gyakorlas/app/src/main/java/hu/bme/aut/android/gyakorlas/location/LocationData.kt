package hu.bme.aut.android.gyakorlas.location

import android.app.Activity
import android.location.Location
import hu.bme.aut.android.gyakorlas.mapData.MapMarker

class LocationData {
    companion object {
        val userLocations = ArrayList<Location>()

        fun initUsers() {
            val location1 = Location("Provider1")
            location1.latitude = 47.4742
            location1.longitude = 19.0592

            val location2 = Location("Provider2")
            location2.latitude = 47.4727
            location2.longitude = 19.0577

            val location3 = Location("Provider3")
            location3.latitude = 47.4744
            location3.longitude = 19.0635

            val location4 = Location("Provider4")
            location3.latitude = 47.4774
            location3.longitude = 19.0460

            val location5 = Location("Provider5")
            location5.latitude = 37.4226
            location5.longitude = -122.0837

            val location6 = Location("Provider5")
            location6.latitude = 37.4220
            location6.longitude = -122.0828

            userLocations.add(location1)
            userLocations.add(location2)
            userLocations.add(location3)
            userLocations.add(location4)
            userLocations.add(location5)
            userLocations.add(location6)
        }
    }
}
