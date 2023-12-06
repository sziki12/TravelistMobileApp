package hu.bme.aut.android.gyakorlas.requestHelp

import hu.bme.aut.android.gyakorlas.mapData.UserMarker

interface RequestHelpListener {
    fun onRequestHelp(userMarker: UserMarker)
}