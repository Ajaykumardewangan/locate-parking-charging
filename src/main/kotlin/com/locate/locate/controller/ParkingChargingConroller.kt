package com.locate.locate.controller

import com.locate.locate.service.ParkingService
import org.json.simple.JSONObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/parking/charging")
class ParkingChargingController(private var parkingService : ParkingService){
    @GetMapping("/stations")
    fun getStations(@RequestParam place:String) : JSONObject = parkingService.getParkingChargingStations(place)
}

