package com.locate.locate.service

import org.json.simple.JSONObject
import org.springframework.http.ResponseEntity


interface ParkingService {
     fun getParkingChargingStations(): ResponseEntity<JSONObject>
}