package com.speertech.testapp.model


fun Int.inWords(): String{
    if (this >1000){
        if (this < 100000){
            val factor = this/1000
            val remainder = (this-(factor*1000))/100
            return "$factor".plus(".").plus("${remainder}K")
        } else if (this in 100001..999999){
            val factor = this/1000
            return "${factor}K"
        }else {
            val factor = this/1000000
            return "${factor}M"
        }
    }
    return toString()
}