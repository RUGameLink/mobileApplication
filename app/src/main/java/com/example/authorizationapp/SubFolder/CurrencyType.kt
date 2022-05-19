package com.example.authorizationapp.SubFolder

enum class CurrencyType(val number: Int) {
    USD(1){
        override fun printName(): String {
            return "Доллар"
        }
    },
    RUB(2){
        override fun printName(): String {
            return "Рубли"
        }
    },
    CNY(3){
        override fun printName(): String {
            return "Юани"
        }
    };
    abstract fun printName(): String
}