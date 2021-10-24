 package com.mahmoud.mysun

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

 class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun bugetsunRiseTimeEvent(view: View) {
           val city=etCityname.text.toString()
       // val url="https://developer.yahoo.com/apps/create/?guccounter=1&guce_referrer=aHR0cHM6Ly9sb2dpbi55YWhvby5jb20v&guce_referrer_sig=AQAAAKkTUHsS3yNjYOyKeEclw9xrKtOHpmR_q4hUNCB6jf70xxKDxcy-c1fn-2ok7PfKa6F1hF5OE2Pyf3l9GiN3BSUQu_dyETHzQ_Ukdsya1dfG7_uD4tNG00HoCBbFd7BRuUCGsMXb74UFLLPNWyHkGsTx8tyTgF-USBx-Sz2WtoMl"
        val url=
            "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        myacynktask().execute(url)
    }

     inner class myacynktask:AsyncTask<String,String,String>(){
         override fun onPreExecute() {
             //pefore run in packground
         }
         override fun doInBackground(vararg params: String?): String {
             //can not acess to ui
             try {
             val url=URL(params[0])
                 val urlconnect=url.openConnection() as HttpURLConnection
                 urlconnect.connectTimeout=700
                 val datajasonstr=convertstreamtostring(urlconnect.inputStream)
                 publishProgress(datajasonstr)
             }catch (ex:Exception){

             }
             return ""
         }

         override fun onProgressUpdate(vararg values: String?) {
             //acess ui
             val json=JSONObject(values[0])
             val query=json.getJSONObject("query")
             val results=query.getJSONObject("results")
             val channel=results.getJSONObject("channel")
             val astronomy=channel.getJSONObject("astronomy")
             val sunrise=astronomy.getString("sunrise")
             tvSunrisetime.text="Sunrise Time:"+sunrise
         }

         override fun onPostExecute(result: String?) {
             //when done process
         }

     }

     fun convertstreamtostring(inputStream: InputStream):String{
         val bufferReader=BufferedReader(InputStreamReader(inputStream))
         var line:String
         var allString:String=""
         do{
             line=bufferReader.readLine()
             if(line!=null)
                 allString+=line
         }while (line!=null)
         return allString
     }
}