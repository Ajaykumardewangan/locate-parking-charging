package com.locate.locate.dao

import org.json.simple.JSONObject
import org.springframework.stereotype.Component

@Component
class LocatinoDAO{
    var locationData = HashMap<String,JSONObject>()

    fun findLocation(place : String): JSONObject{
        var result  = JSONObject()
        if(locationData.containsKey(place)){
            result = locationData.get(place) as JSONObject
        }
        return result
    }
    fun setLocation(place : String, locationDetails : JSONObject){
        if(!locationData.containsKey(place)){
            locationData.put(place, locationDetails)
        }
    }
}