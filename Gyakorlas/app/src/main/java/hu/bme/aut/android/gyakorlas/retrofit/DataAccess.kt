package hu.bme.aut.android.gyakorlas.retrofit

import android.util.Log
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


object DataAccess {
        fun startLoginListener(
            user: UserData,
            onSuccess: () -> Unit,
            onFailure: (message: String) -> Unit,
            onUserNotExists: () -> Unit
        ) {
            val connection = Connection()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = connection.userAPI.loginUser(user)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            onSuccess.invoke() // Invoke success callback
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
            }
        }

    fun startRegistrationListener( user: UserData,
                                   onSuccess: () -> Unit,
                                   onFailure: (message: String) -> Unit,
                                   onUserExists: () -> Unit)
    {
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
        data class UserData(
            val email: String,
            val password: String
        )

        interface UserAccessAPI {
            //@Headers("Content-Type: application/json")
            @POST("/api/login")
            suspend fun loginUser(@Body requestBody: UserData): Response<ResponseBody>
            @POST("/api/registration")
            suspend fun registerUser(@Body requestBody: UserData): Response<ResponseBody>
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