package com.locate.locate.service

import org.json.simple.JSONObject

interface ParkingService {
     fun getParkingChargingStations(place:String): JSONObject
}