package com.example.smsapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class OtpReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.action)) {
            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status!!.getStatusCode()) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    val verificationCode = getVerificationCode(message)
                    val broadcastOtpIntnet = Intent(Constants.ACTION_SEND_MESSAGE)
                    broadcastOtpIntnet.putExtra(Constants.OTP, verificationCode)
                    context.sendBroadcast(broadcastOtpIntnet)
                }
                CommonStatusCodes.TIMEOUT -> {
                }
            }// Waiting for SMS timed out (5 minutes)
            // Handle the error ...
        }
    }

    //Method to extract the code from incoming message content
    //This method extract the code in such a way that ,take the
    //characters  between two colon(:)

         private fun getVerificationCode(message: String): String? {
        var verificationCode: String? = null
        val indexStart = message.indexOf(":")
        val indexEnd = message.indexOf(":", indexStart + 1)

        if (indexEnd != -1) {
            val start = indexStart + 2
            val end = indexEnd - 2
            verificationCode = message.substring(start, end)

            return verificationCode
        }

        return verificationCode
    }

}
