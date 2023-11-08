package hu.bme.aut.android.gyakorlas.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


class DataAccess {
    fun buid()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://travelist-red.vercel.app/api")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val currencyAPI = retrofit.create(UserAccessAPI::class.java)
        btnGetRate.setOnClickListener {
            val ratesCall = currencyAPI.loginUser("Email","Password")
            ratesCall.enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    tvResult.text = t.message
                }
                override fun onResponse(call: Call<User>,
                                        response: Response<User>
                ) {
                    tvResult.text = response.body()?.rates?.HUF.toString()
                }
            })
        }
    }

}

interface UserAccessAPI {
    @POST("/login")
    fun loginUser(@Query("name") email: String,password:String): Call<User>
}