package com.example.authorizationapp.SubFolder

import android.content.Context
import com.example.authorizationapp.R

enum class SubscriptionType(val number: Int) {
    TEMPORARY(1){
        override fun printName(context : Context): String {

            return  context.getString(R.string.timeType)
        }

    }, PERMANENT(2){
        override fun printName(context : Context): String {
            return context.getString(R.string.permanentType)
        }
    };
    abstract fun printName(context : Context): String
}