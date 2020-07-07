package com.locate.locate.utility

import com.locate.locate.model.Position
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.springframework.stereotype.Component

@Component
class CommonUtils{
    fun parseJsonResponse(parkingJson : JSONObject):JSONObject{
        if(!parkingJson.isNullOrEmpty()){
            var newJson  = parkingJson.get("results") as JSONObject
            if( ! newJson.isNullOrEmpty() ) {
                var jsonArr = newJson.get("items") as JSONArray
                if(!jsonArr.isNullOrEmpty()) {
                    newJson = jsonArr.get(0) as JSONObject
                    return newJson
                }
            }
        }
       return JSONObject()
    }
    fun parseAltitudeLongitude(placeJson : JSONObject): Position {
        if(!placeJson.isNullOrEmpty()){
            var newJson  = placeJson.get("Response") as JSONObject
            if( ! newJson.isNullOrEmpty() ) {
                var jsonArr = newJson.get("View") as JSONArray
                if(!jsonArr.isNullOrEmpty()) {
                    newJson = jsonArr.get(0) as JSONObject
                    jsonArr = newJson.get("Result") as JSONArray
                    newJson = jsonArr.get(0) as JSONObject
                    newJson = newJson.get("Location") as JSONObject
                    newJson = newJson.get("DisplayPosition") as JSONObject
                    var position = Position()
                    position.altitude = newJson.get("Latitude").toString()
                    position.longitude = newJson.get("Longitude").toString()
                    return position
                }
            }
        }
        return Position()
    }
}