package com.locate.locate.controller

import com.locate.locate.service.LocationTraceService
import org.json.simple.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trace/parking/charging/eatdrink")
class LocationTraceController(){

    @Autowired
    lateinit var locationTraceService : LocationTraceService

    @GetMapping("/stations")
    fun getStations(@RequestParam place:String) : JSONObject = locationTraceService.getStations(place)
}

