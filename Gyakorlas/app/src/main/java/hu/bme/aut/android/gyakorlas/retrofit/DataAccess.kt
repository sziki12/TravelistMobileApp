package hu.bme.aut.android.gyakorlas.retrofit

import android.content.Context
import android.util.JsonToken
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import hu.bme.aut.android.gyakorlas.getCurrentUser
import hu.bme.aut.android.gyakorlas.mapData.MapMarker
import hu.bme.aut.android.gyakorlas.mapData.PlaceData
import hu.bme.aut.android.gyakorlas.mapData.UserMarker

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import java.util.EnumMap


object DataAccess {
    private val isWorkInProgress:EnumMap<Process,Boolean> = EnumMap(Process::class.java)
    init {
        for(process in Process.entries)
        {
            isWorkInProgress[process] = false
        }
    }

    enum class Process
    {
        Login, Registration, UploadNewPlace, GetPlaces, UploadHelpMessage, GetUserMarkers
    }
        fun startLoginListener(
            user: UserServerData,
            onSuccess: (token: String) -> Unit,
            onFailure: (message: String) -> Unit,
            onUserNotExists: () -> Unit
        ) {
            if(isWorkInProgress[Process.Login]!=false)
                return

            isWorkInProgress[Process.Login]=true
            val connection = Connection()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = connection.userAPI.loginUser(user)
                    val token = response.headers()["set-cookie"]
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            if (token != null) {
                                Log.i("DATAACCESSTOKEN", token)
                                onSuccess.invoke(token)
                            } // Invoke success callback
                            val responseBody = response.body() // Access the response body here
                            // Process responseBody as needed
                        }else if(response.code()==400)
                        {
                            onUserNotExists()
                        } else {
                            onFailure.invoke("Request failed with code: ${response.code()}")
                            Log.i("Retrofit","Request failed with code: ${response.code()}")
                        }
                    }
                } catch (e: Exception) {
                    onFailure.invoke("Request failed: ${e.message}")
                    Log.i("Retrofit","Request failed: ${e.message}")
                }
                isWorkInProgress[Process.Login]=false
            }
        }

    fun startRegistrationListener( user: RegistrationServerData,
                                   onSuccess: () -> Unit,
                                   onFailure: (message: String) -> Unit,
                                   onUserExists: () -> Unit)
    {
        if(isWorkInProgress[Process.Registration]!=false)
            return

        isWorkInProgress[Process.Registration]=true

        val connection = Connection()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = connection.userAPI.registerUser(user)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        onSuccess.invoke() // Invoke success callback
                        val responseBody = response.body() // Access the response body here
                        // Process responseBody as needed
                    }else if(response.code()==400)
                    {
                        onUserExists()
                    } else {
                        onFailure.invoke("Request failed with code: ${response.code()}")
                        Log.i("Retrofit","Request failed with code: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                onFailure.invoke("Request failed: ${e.message}")
                Log.i("Retrofit","Request failed: ${e.message}")
            }
            isWorkInProgress[Process.Registration]=false
        }
    }

    fun startUploadNewPlaceListener( place: PlaceServerData,
                                     onSuccess: () -> Unit,
                                     onFailure: (message: String) -> Unit)
    {
        if(isWorkInProgress[Process.UploadNewPlace]!=false)
            return

        isWorkInProgress[Process.UploadNewPlace]=true

        val connection = Connection()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = connection.userAPI.uploadNewPlace(place)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        onSuccess.invoke() // Invoke success callback
                        val responseBody = response.body() // Access the response body here
                        // Process responseBody as needed
                    } else {
                        onFailure.invoke("Request failed with code: ${response.code()}")
                        Log.i("Retrofit","Request failed with code: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                onFailure.invoke("Request failed: ${e.message}")
                Log.i("Retrofit","Request failed: ${e.message}")
            }
            isWorkInProgress[Process.UploadNewPlace]=false
        }
    }

    fun getMapMarkers(onSuccess: (markers:ArrayList<MapMarker>?) -> Unit)
    {
        if(isWorkInProgress[Process.GetPlaces]!=false)
            return

        isWorkInProgress[Process.GetPlaces]=true

        val connection = Connection()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = connection.userAPI.getPlaces()
                if (response.isSuccessful) {
                   val outMarkers = ArrayList<MapMarker>()
                   for(place in response.body()!!.places!!)
                   {
                       //TODO Comments and Rating
                       outMarkers.add(MapMarker(PlaceData(place.name,place.location,place.description),place.latitude,place.longitude))
                       Log.i("Retrofit","Place: $place")
                   }
                    onSuccess(outMarkers)
                } else {

                    Log.i("Retrofit","Request failed with code: ${response.code()}")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.i("Retrofit", "Request failed: ${e.message}")
                }
            }
            isWorkInProgress[Process.GetPlaces]=false
        }
    }

    fun startHelpMessageListener( userMarker: UserMarkerServerData,
                                     onSuccess: () -> Unit,
                                     onFailure: (message: String) -> Unit)
    {
        if(isWorkInProgress[Process.UploadHelpMessage]!=false)
            return

        isWorkInProgress[Process.UploadHelpMessage]=true

        val connection = Connection()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = connection.userAPI.uploadNewHelpMessage(userMarker)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        onSuccess.invoke() // Invoke success callback
                        val responseBody = response.body() // Access the response body here
                        // Process responseBody as needed
                    } else {
                        onFailure.invoke("Request failed with code: ${response.code()}")
                        Log.i("Retrofit","Request failed with code: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                onFailure.invoke("Request failed: ${e.message}")
                Log.i("Retrofit","Request failed: ${e.message}")
            }
            isWorkInProgress[Process.UploadHelpMessage]=false
        }
    }

    fun getUserMarkers(onSuccess: (markers:ArrayList<UserMarker>?) -> Unit)
    {
        if(isWorkInProgress[Process.GetUserMarkers]!=false)
            return

        isWorkInProgress[Process.GetUserMarkers]=true

        val connection = Connection()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = connection.userAPI.getUserMarkers()
                Log.i("GETUSERMARKERS", response.toString())
                if (response.isSuccessful) {
                    val outMarkers = ArrayList<UserMarker>()
                    for(userMarker in response.body()!!.requestHelps!!)
                    {
                        outMarkers.add(UserMarker(userMarker.userId, userMarker.latitude, userMarker.longitude, userMarker.message))
                        Log.i("USERMARKER", userMarker.message)
                        Log.i("Retrofit","UserMarker: $userMarker")
                    }
                    onSuccess(outMarkers)
                } else {

                    Log.i("Retrofit","Request failed with code: ${response.code()}")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.i("Retrofit", "Request failed: ${e.message}")
                }
            }
            isWorkInProgress[Process.GetUserMarkers]=false
        }
    }

        class Connection
        {
            val client = OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://travelist-red.vercel.app/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
                .build()

            val userAPI = retrofit.create(UserAccessAPI::class.java)
        }

    @Serializable
    data class UserServerData(
        val email: String,
        val password: String
    )

    @Serializable
    data class RegistrationServerData(
        val username: String,
        val email: String,
        val password: String
    )

    @Serializable
    data class PlaceServerData(
        val name: String,
        val location: String,
        val latitude: Double,
        val longitude: Double,
        val description: String,
        val rating: Double
    )

    @Serializable
    data class UserMarkerServerData(
        var token: String,
        val latitude: Double,
        val longitude: Double,
        val message: String
    )

    @Serializable
    data class UserMarkerData(
        val userId: String,
        val latitude: Double,
        val longitude: Double,
        val message: String
    )

    @Serializable
    data class PlaceServerArray(val places :List<PlaceServerData>?)

    @Serializable
    data class UserMarkerServerArray(val requestHelps :List<UserMarkerData>?)

        interface UserAccessAPI {
            //@Headers("Content-Type: application/json")
            @POST("/api/login")
            suspend fun loginUser(@Body requestBody: UserServerData): Response<ResponseBody>
            @POST("/api/registration")
            suspend fun registerUser(@Body requestBody: RegistrationServerData): Response<ResponseBody>
            @POST("/api/places")
            suspend fun uploadNewPlace(@Body requestBody: PlaceServerData): Response<ResponseBody>
            @GET("/api/places")
            suspend fun getPlaces(): Response<PlaceServerArray>
            @POST("/api/request-help")
            suspend fun uploadNewHelpMessage(@Body requestBody: UserMarkerServerData): Response<ResponseBody>
            @GET("/api/request-help")
            suspend fun getUserMarkers(): Response<UserMarkerServerArray>
        }

        class LoggingInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request: Request = chain.request()
                val t1 = System.nanoTime()
                Log.d(
                    "TAG_HTTP", java.lang.String.format(
                        "Sending request %s on %s%n%s",
                        request.url, chain.connection(), request.headers
                    )
                )
                Log.d("TAG_HTTP", "Request body: " + parseRequest(request))

                val response: okhttp3.Response = chain.proceed(request)
                val t2 = System.nanoTime()
                Log.d(
                    "TAG_HTTP", java.lang.String.format(
                        "Received response for %s in %.1fms%n%s%s",
                        response.request.url, (t2 - t1) / 1e6, response.headers, response.message
                    )

                )
                return response
            }
            private fun parseRequest(request: Request): String {
                val buffer = Buffer()
                try {
                    val copy = request.newBuilder().build()
                    copy.body?.writeTo(buffer)
                } catch (e: IOException) {
                    Log.d("TAG_HTTP", "Parse Error")
                }
                return buffer.readUtf8()
            }
        }
    }