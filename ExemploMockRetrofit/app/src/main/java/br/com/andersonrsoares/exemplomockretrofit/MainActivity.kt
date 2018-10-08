package br.com.andersonrsoares.exemplomockretrofit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Api.create(this)?.cep("81900350")?.enqueue(object:Callback<Cep>{
            override fun onFailure(call: Call<Cep>, t: Throwable) {
              t.printStackTrace()
            }

            override fun onResponse(call: Call<Cep>, response: Response<Cep>) {
               print("teste")
            }
        } )
    }
}
