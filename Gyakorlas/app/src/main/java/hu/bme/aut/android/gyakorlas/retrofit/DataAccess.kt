package hu.bme.aut.android.gyakorlas.retrofit

import android.widget.Button
import com.squareup.moshi.JsonClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query


class DataAccess {
    companion object
    {
        fun registerLoginListener(btnLogin: Button,onSuccess:()->Unit,onFailure:(message:String)->Unit,onWrongUser:()->Unit)
        {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://travelist-red.vercel.app/api")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val currencyAPI = retrofit.create(UserAccessAPI::class.java)
            btnLogin.setOnClickListener {
                val ratesCall = currencyAPI.loginUser("Email","Password")
                ratesCall.enqueue(object: Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        //Faliure
                        onFailure(t.message.toString())
                    }
                    override fun onResponse(call: Call<User>,
                                            response: Response<User>
                    ) {
                        if(response.body()?.message?.any() == true)
                        {
                            //No User
                            onWrongUser()
                            //response.body()?.email.toString()
                            //response.body()?.name.toString()

                        }
                        else
                        {
                            //Success
                            onSuccess()
                        }
                    }
                })
            }
        }
    }


}

@JsonClass(generateAdapter = true)
class User(val email: String?, val password: String?, val message:String?)


interface UserAccessAPI {
    @POST("/login")
    fun loginUser(@Query("name") email: String,password:String): Call<User>
}