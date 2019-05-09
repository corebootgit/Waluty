package com.example.waluty

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val year = calendarView.year

        val cmonth = String.format("%02d",calendarView.month )
        val cday = String.format("%02d",calendarView.dayOfMonth )

        val dt: String = year.toString() + "-$cmonth-$cday"

        //Toast.makeText(this@MainActivity, dt, Toast.LENGTH_SHORT).show()
        getCurrency("USD", dt)



    calendarView.setOnDateChangedListener { view, year, month, dayOfMonth ->
        // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
        val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year

        val cmonth = String.format("%02d",calendarView.month+1 )
        val cday = String.format("%02d",calendarView.dayOfMonth )

        val dt: String = year.toString() + "-$cmonth-$cday"

        Toast.makeText(this@MainActivity, dt, Toast.LENGTH_SHORT).show()
        getCurrency("USD", dt)
        //viewText.text = String.format("%02d",calendarView.dayOfMonth )
    }

//        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
//            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
//            val dt: String = "" + year + "-"+(month+1)+"-" + dayOfMonth
//            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
//            //getCurrency("USD", dt)
//            viewText.text = calendarView.date.toString()
//        }

        //viewText.text = calendarView.date.toString()




        //getCurrency("USD", "2019-04-01")


    }

    fun getCurrency (code: String, date: String) {
        val task = GetHTTPTask(code, date)
        task.execute()
    }


    inner class GetHTTPTask(code: String, date: String) : AsyncTask<Void, Void, String>() {

        private val currCode = code
        private val currDate = date

        override fun doInBackground(vararg params: Void?): String? {

            //val url = URL("http://api.nbp.pl/api/exchangerates/rates/A/USD/2019-04-01/?format=json")
            val url = URL("http://api.nbp.pl/api/exchangerates/rates/A/$currCode/$currDate/?format=json")
            val urlConnection = url.openConnection() as HttpURLConnection

            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    val restext = urlConnection.inputStream.bufferedReader().readText()
                    Log.d("UrlTest", restext)
                   // viewText.text = restext
                    return restext
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }
            else {
             println("ERROR ${urlConnection.responseCode}")
            }
             return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

//            viewText.text = JSONObject(result).toString()
//
//            val jsonObj = JSONObject(result)
//            val jsonArray = jsonObj.getString("rates")
//
//            val sObject = jsonArray.get(0).toString()
//            val mItemObject = JSONObject(sObject)
//
//            // Get String value from json object
//            val kurs = mItemObject.getString("mid")

            if(result != null) {

                val index = result!!.indexOf("mid")

                val kurs: String = result.substring(index + 5..index + 10)

                kursView.text = kurs + " PLN"

                //viewText.text = kurs
            }
            else
            {
                kursView.text = "Brak kursu"
            }

            }




    }

}


