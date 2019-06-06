package com.example.smsapi

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever

class SMSRetrievalClient(smsReceivedStatus: SmsReceivedStatus) {
    private var smsReceivedStatus: SmsReceivedStatus = smsReceivedStatus
    private var status: Boolean = true

/*Creation of sms retriever client*/
/* it should be done at the same time we make a request to
   server for the otp*/

    fun getSmsRetrieverClient(context: Context) {
        val client = SmsRetriever.getClient(context)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            smsReceivedStatus.receivedSuccessfully(status)
            println("message recived succesfully")
        }
        task.addOnFailureListener {
            smsReceivedStatus.receivingFailed(status)
            println("message reciving failed")
        }
    }

}