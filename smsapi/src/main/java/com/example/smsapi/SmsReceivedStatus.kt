package com.example.smsapi

public interface SmsReceivedStatus{
    /*Successful message receiving make the status value true*/
    fun receivedSuccessfully(status:Boolean)
    /*Unsuccessful message receiving make the status value true*/
    fun receivingFailed(status: Boolean)

}