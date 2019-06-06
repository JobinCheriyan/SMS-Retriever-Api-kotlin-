package com.example.smsretrieverapikotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smsapi.*
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SmsReceivedStatus {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*We need to attach the hash code of our app alone with the SMS
        * then only our app could receive the message
        * so that we should generate the hash code first */
        val packageName = packageName
        /*To generate the hash code create an object of AppSignature by passing
        * context and packageName*/
        var appSignature: AppSignature = AppSignature(this, packageName)
        /*Receive the hash code in an ArrayList*/
        var hashCode: ArrayList<String> = appSignature.appSignatures

        /*Sms retrieval client should be created whenever you make a request for the Otp message*/
        var smsRetrievalClient: SMSRetrievalClient = SMSRetrievalClient(this)
        smsRetrievalClient.getSmsRetrieverClient(this)

        /*Whenever the phone receives a message with the hash code of your application,
        * it will be processed inside your app and Otp code will be generated.
        * Generated Otp will be broadcasted and can receive anywhere using the action ACTION_SEND_MESSAGE*/
        var intentFilter: IntentFilter = IntentFilter(Constants.ACTION_SEND_MESSAGE)
        registerReceiver(broadcastReceiver, intentFilter)


    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun receivedSuccessfully(status: Boolean) {
        println(" Success status $status")
    }

    override fun receivingFailed(status: Boolean) {
        println(" failed status $status")
    }

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action.equals(Constants.ACTION_SEND_MESSAGE)) {
                var value: String = intent.getStringExtra(Constants.OTP)
                Toast.makeText(context, value, Toast.LENGTH_LONG).show()
            }
        }

    }

}

