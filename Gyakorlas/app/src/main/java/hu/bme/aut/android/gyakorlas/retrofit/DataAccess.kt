package hu.bme.aut.android.gyakorlas.retrofit

import android.util.Log
import android.widget.Button
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


class DataAccess {
    companion object
    {
        fun registerLoginListener(user:UserData,onSuccess:()->Unit,onFailure:(message:String)->Unit,onWrongUser:()->Unit)
        {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://travelist-red.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

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
                            Log.i("Retrofit","Unknown Response: ${response.code()}")
                        }
                    }
                })

           // }
        }
    }
}

data class UserData(
    var email: String,
    var password: String
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
    @POST("login")
    fun loginUser(@Body user:UserData): Call<Void>
}