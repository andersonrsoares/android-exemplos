package br.com.andersonrsoares.exemplomockretrofit

/**
 * Created by devmaker on 06/03/18.
 */

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import com.google.gson.annotations.SerializedName
import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor
import ir.mirrajabi.okhttpjsonmock.providers.InputStreamProvider
import okhttp3.*
import retrofit2.Call
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.GET
import java.io.IOException
import java.io.InputStream


data class Cep(
        @SerializedName("cep") var cep: String,
        @SerializedName("logradouro") val logradouro: String,
        @SerializedName("complemento") val complemento: String,
        @SerializedName("bairro") val bairro: String,
        @SerializedName("localidade") val localidade: String,
        @SerializedName("uf") val uf: String,
        @SerializedName("unidade") val unidade: String,
        @SerializedName("ibge") val ibge: String,
        @SerializedName("gia") val gia: String
)


interface Api {

    @GET("{cep}/json")
    fun cep(@Path("cep") cep:String): Call<Cep>

    companion object Factory {

        val URL = "https://viacep.com.br/"

        private fun getAndroidProvider(context:Context): InputStreamProvider {
            return object : InputStreamProvider{
                override fun provide(path: String?): InputStream {
                    try {
                        return context.assets.open(path!!)
                    }catch (e:Exception){
                        return context.assets.open("error.json")
                    }
                }
            }
        }

        fun create(context:Context): Api? {
//
//            val logInterceptor = HttpLoggingInterceptor()
//            logInterceptor.level = HttpLoggingInterceptor.Level.BODY


            try {
                val okHttpClient = OkHttpClient().newBuilder()
                        .connectTimeout(6000, TimeUnit.MILLISECONDS)
                        .readTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
                        .writeTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
                        //.addInterceptor(logInterceptor)
                        // .addInterceptor(headintercepter)
                        .addInterceptor(OkHttpMockInterceptor(getAndroidProvider(context), 5))
                        .build()


                val gson = GsonBuilder()
                        .create()


                val retrofit = Retrofit.Builder()
                        .baseUrl(URL)
                        .client(okHttpClient)
                       // .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()

                return retrofit.create(Api::class.java)
            }catch (e:Exception){
                e.printStackTrace()
                return null
            }

        }
    }



}
