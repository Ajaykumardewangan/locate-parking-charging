package com.locate.locate.service.serviceImpl

import com.locate.locate.dao.LocationDAO
import com.locate.locate.model.AppProperties
import com.locate.locate.model.Position
import com.locate.locate.service.ParkingService
import com.locate.locate.utility.CommonUtils
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*


@Service
class ParkingServiceImpl():ParkingService {

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var commonJsonUtils: CommonUtils

    @Autowired
    lateinit var locationDao : LocationDAO

    override fun getParkingChargingStations(place:String): JSONObject {
        var resultJson = JSONObject()
        resultJson =  locationDao.findLocation(place)
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
         var restTemplate = RestTemplate()
         var headers =  HttpHeaders()
         headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(appProperties.searchCategs)
                .queryParam("at",position.altitude+","+position.longitude)
                .queryParam("cat",appProperties.parking)
                .queryParam("apiKey",appProperties.apiKey)
         var entity = HttpEntity<String>(headers)
         var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
         var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
         return  commonJsonUtils.parseJsonResponse(jsonObject)
    }

    @Async
     fun getChargingStations(position: Position): JSONObject {
         var restTemplate = RestTemplate()
         var headers =  HttpHeaders()
         headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
         var builder = UriComponentsBuilder.fromHttpUrl(appProperties.searchCategs)
                 .queryParam("at",position.altitude+","+position.longitude)
                 .queryParam("cat",appProperties.charging)
                 .queryParam("apiKey",appProperties.apiKey)
         var entity = HttpEntity<String>(headers)
         var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
        var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
        return commonJsonUtils.parseJsonResponse(jsonObject)
    }

    @Async
    fun getEatDrinkPlace(position: Position): JSONObject {
        var restTemplate = RestTemplate()
        var headers =  HttpHeaders()
        headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(appProperties.searchCategs)
                .queryParam("at",position.altitude+","+position.longitude)
                .queryParam("cat",appProperties.eatDrink)
                .queryParam("apiKey",appProperties.apiKey)
        var entity = HttpEntity<String>(headers)
        var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
        var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
        return commonJsonUtils.parseJsonResponse(jsonObject)
    }
    fun getAltitudeAndLongitudeByPlace( place : String): Position {
        var restTemplate = RestTemplate()
        var headers =  HttpHeaders()
        headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(appProperties.searchPlace)
                .queryParam("searchtext",place)
                .queryParam("gen",9)
                .queryParam("apiKey",appProperties.apiKey)
        var entity = HttpEntity<String>(headers)
        var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
        var  result1 = result.body as String
        var parser =  JSONParser()
        var jsonObject = parser.parse(result1) as JSONObject
        return commonJsonUtils.parseAltitudeLongitude(jsonObject)
    }
}



