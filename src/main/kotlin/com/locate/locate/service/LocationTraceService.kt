package com.locate.locate.service

import org.json.simple.JSONObject

interface LocationTraceService {
     fun getStations(place:String): JSONObject
}