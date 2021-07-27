package com.example.retrofitpostnaverapi

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Client_ID = "ALo3g0LzW7v05w9jZf6i"
        val Client_Secret = "2KhgqeGEen"
        val BASE_URL_NAVER_API = "https://openapi.naver.com/"

        var newsTitle = findViewById<TextView>(R.id.txtnewTitle)
        var newsDescription = findViewById<TextView>(R.id.txtnewDescription)
        var engTrans = findViewById<TextView>(R.id.txtTrans)
        var korTrans = findViewById<EditText>(R.id.ettTrans)
        var tranButton = findViewById<Button>(R.id.btnTrans)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(NaverAPI::class.java)
        val callGetSearchNews = api.getSearchNews(Client_ID, Client_Secret, "테스트")

        tranButton.setOnClickListener {
            var tran_text = korTrans.text.toString()
            val callPostTransferPapago = api.transferPapago(Client_ID, Client_Secret, "en", "ko",tran_text)

            callPostTransferPapago.enqueue(object : Callback<ResultTransferPapago>{
                override fun onResponse(
                    call: Call<ResultTransferPapago>,
                    response: Response<ResultTransferPapago>
                ) {
                    engTrans.text = response.body()!!.message.result.translatedText.toString()
                }

                override fun onFailure(call: Call<ResultTransferPapago>, t: Throwable) {

                }
            })
        }

        callGetSearchNews.enqueue(object : Callback<ResultGetSearchNews>{
            override fun onResponse(
                call: Call<ResultGetSearchNews>,
                response: Response<ResultGetSearchNews>) {
                var result = response.body()
                var itemResult = result!!.items
                newsTitle.text = itemResult[0].title
                newsDescription.text = itemResult[0].description
            }

            override fun onFailure(call: Call<ResultGetSearchNews>, t: Throwable) {

            }
        })

    }
}