package hu.bme.aut.android.gyakorlas.retrofit

import android.util.Log

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder

class DataAccess {
    companion object
    {
        fun registerLoginListener(user:UserData,onSuccess:()->Unit,onFailure:(message:String)->Unit,onWrongUser:()->Unit)
        {

            val client = OkHttpClient.Builder().addInterceptor(
                LoggingInterceptor()).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://travelist-red.vercel.app/")
                //.baseUrl(BuildConfig.NEWS_BASE_URL)
                .client(client)
                //.addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()) )
                /*.addConverterFactory(
                        Json{ ignoreUnknownKeys = true }.asConverterFactory(
                            "application/json".toMediaType()) )*/
                .build()

            // Create JSON using JSONObject
            //val jsonObject = JsonObject()
            //jsonObject.put("email", user.email)
            //jsonObject.put("password", user.password)

            val jsonObjectString = Json.encodeToString(user)//encodeToString(jsonObject)

            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val userAPI = retrofit.create(UserAccessAPI::class.java)

                val userCall = userAPI.loginUser(requestBody)
                userCall.enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        //Faliure
                        onFailure(t.message.toString())
                    }
                    override fun onResponse(call: Call<Void>,
                                            response: Response<Void>
                    ) {
                        if(response.code()==400)
                        {
                            //No User
                            onWrongUser()
                        }
                        else if(response.isSuccessful)
                        {
                            //Success
                            onSuccess()
                        }
                        else
                        {
                            Log.i("TAG_HTTP","Unknown Response: ${response.code()}")
                        }
                    }
                })
        }
    }
}

@Serializable
data class UserData(
    val email: String,
    val password: String
)


interface UserAccessAPI {
    //@Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body  requestBody: RequestBody): Call<Void>
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
                "Received response for %s in %.1fms%n%s",
                response.request.url, (t2 - t1) / 1e6, response.headers
            )
        )
        return response
    }

    private fun parseRequest(request: Request):String
    {
        val buffer = Buffer()
        try {
            val copy = request.newBuilder().build()
            copy.body?.writeTo(buffer)
        }
        catch(e:IOException)
        {
            Log.d("TAG_HTTP","Parse Error")
        }
        return buffer.readUtf8()
    }
}