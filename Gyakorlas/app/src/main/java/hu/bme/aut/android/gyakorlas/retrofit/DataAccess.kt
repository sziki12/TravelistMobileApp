package hu.bme.aut.android.gyakorlas.retrofit

import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException


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
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()) )
                /*.addConverterFactory(
                        Json{ ignoreUnknownKeys = true }.asConverterFactory(
                            "application/json".toMediaType()) )*/
                .build()

            /*val retrofit = Retrofit.Builder()
                .baseUrl("https://travelist-red.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            */
            val userAPI = retrofit.create(UserAccessAPI::class.java)

            //if(user.email!=null&&user.password!=null)
            //{
                val userCall = userAPI.loginUser(user)
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

           // }

        }
    }
}

data class UserData(
    val email: String,
    val password: String
)


//data class User(@Json(name = "email") val email: String?,
          // @Json(name = "password") val password: String?,
          // @Json(name = "message")val message:String?)

/*data class User(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "message")
    val message: String?
)*/



interface UserAccessAPI {
    //@Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body user:UserData): Call<Void>
}

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var gson = Gson()
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        Log.d("TAG_HTTP", java.lang.String.format("Sending request %s on %s%n%s",
            request.url(), chain.connection(), request.headers()))
        Log.d("TAG_HTTP","Request body: "+request.body().toString())
        Log.d("TAG_HTTP","Response body: "+gson.fromJson(request.body(),UserData::class.java))

        val response: okhttp3.Response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.d("TAG_HTTP", java.lang.String.format("Received response for %s in %.1fms%n%s",
            response.request().url(), (t2 - t1) / 1e6, response.headers()))
        //Log.d("TAG_HTTP","Response body: "+gson.fromJson(response.body().toString(),UserData::class.java).email)
        /*val resBody = JSONArray(response.body())
        for(i in 0..resBody.length())
        {
            Log.d("TAG_HTTP","Request body: "+resBody[i])
        }*/
        return response
    }
}