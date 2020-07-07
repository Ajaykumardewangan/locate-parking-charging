package com.locate.locate.service.serviceImpl

import com.locate.locate.dao.LocatinoDAO
import com.locate.locate.model.Position
import com.locate.locate.service.ParkingService
import com.locate.locate.utility.CommonUtils
import org.apache.tomcat.util.codec.binary.Base64
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class ParkingServiceImpl(private val commonJsonUtils: CommonUtils,
                         private val locationDao : LocatinoDAO):ParkingService {

    override fun getParkingChargingStations(place:String): JSONObject {
        print("This is a place given by user : $place")
        var resultJson = JSONObject()
        resultJson =  locationDao.findLocation(place)
        println(resultJson.toString())
        if(resultJson.isNullOrEmpty()) {
            var position = getAltitudeAndLongitudeByPlace(place)
            if (!position.isEmplty()) {
                resultJson.put("Parking_Place", getParkingPlace(position))
                resultJson.put("Charging_Station", getChargingStations(position))
                resultJson.put("Eat_Drink_Place", getEatDrinkPlace(position))
                locationDao.setLocation(place, resultJson)
            } else
                resultJson.put("Description", "No result found for this location")
        }
        return resultJson;
    }

    @Async
    fun getParkingPlace(position: Position): JSONObject {
         var url = "https://places.ls.hereapi.com/places/v1/discover/explore"
         var restTemplate = RestTemplate()
         var headers =  HttpHeaders()
         headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("at",position.altitude+","+position.longitude)
                .queryParam("cat","parking-facility")
                .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
         var entity = HttpEntity<String>(headers)
         var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
         var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
         return  commonJsonUtils.parseJsonResponse(jsonObject)
    }

    @Async
     fun getChargingStations(position: Position): JSONObject {
         var url = "https://places.ls.hereapi.com/places/v1/discover/explore"
         var restTemplate = RestTemplate()
         var headers =  HttpHeaders()
         headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
         var builder = UriComponentsBuilder.fromHttpUrl(url)
                 .queryParam("at",position.altitude+","+position.longitude)
                 .queryParam("cat","ev-charging-station")
                 .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
         var entity = HttpEntity<String>(headers)
         var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
        var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
        return commonJsonUtils.parseJsonResponse(jsonObject)

    }

    @Async
    fun getEatDrinkPlace(position: Position): JSONObject {
        var url = "https://places.ls.hereapi.com/places/v1/discover/explore"
        var restTemplate = RestTemplate()
        var headers =  HttpHeaders()
        headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("at",position.altitude+","+position.longitude)
                .queryParam("cat","eat-drink")
                .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
        var entity = HttpEntity<String>(headers)
        var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
        var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
        return commonJsonUtils.parseJsonResponse(jsonObject)
    }
    fun getAltitudeAndLongitudeByPlace( place : String): Position {
        var url = "https://geocoder.ls.hereapi.com/6.2/geocode.json"
        var restTemplate = RestTemplate()
        var headers =  HttpHeaders()
        headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("searchtext",place)
                .queryParam("gen",9)
                .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
        var entity = HttpEntity<String>(headers)
        var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
        var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
        return commonJsonUtils.parseAltitudeLongitude(jsonObject)
    }
}



