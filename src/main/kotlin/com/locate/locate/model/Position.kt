package com.locate.locate.model

class Position{
    var altitude:String = ""
    var longitude:String = ""
    fun isEmplty():Boolean {
        if (altitude == "" || longitude == "")
            return true
        return false
    }
}
