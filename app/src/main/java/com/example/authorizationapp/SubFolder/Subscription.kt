package com.example.authorizationapp.SubFolder

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Date
import java.time.LocalDate

class Subscription {
    var id: String = "0"
    set(value){field = value}
    get() = field
    var title: String = ""
        set(value){
            if(value.isEmpty()){
                throw Exception("Пустое значение имени")
            }
            field = value
        }
        get(){
            return field
        }
    var cost: Int = 1
        set(value){
            field = value
        }
        get(){
            return field
        }
    var firstDate: String
        @RequiresApi(Build.VERSION_CODES.O)
        set(value){
            field = value
        }
        get(){
            return field
        }
    var lastDate: String
        @RequiresApi(Build.VERSION_CODES.O)
        set(value){
            field = value
        }
        get(){
            return field
        }
    var type: SubscriptionType = SubscriptionType.TEMPORARY
        set(value){
            field = value
        }
        get() {return field}
    var currency: CurrencyType = CurrencyType.RUB
        set(value) {
            field = value
        }
        get() = field
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(){
        this.title = "title"
        this.cost = 1
        this.firstDate = ""
        this.lastDate = ""
        this.type = SubscriptionType.TEMPORARY
    }
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(title: String, cost: Int, firstDate: String, lastDate: String, type: SubscriptionType){
        this.title = title
        this.cost = cost
        this.firstDate = firstDate
        this.lastDate = lastDate
        this.type = type
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        id: String,
        title: String,
        cost: Int,
        firstDate: String,
        lastDate: String,
        type: SubscriptionType,
        currency: CurrencyType
    ) {
        this.id = id
        this.title = title
        this.cost = cost
        this.firstDate = firstDate
        this.lastDate = lastDate
        this.type = type
        this.currency = currency
    }
}