package com.locate.locate.service.serviceImpl

import com.locate.locate.dao.LocationDAO
import com.locate.locate.model.AppProperties
import com.locate.locate.model.Position
import com.locate.locate.service.LocationTraceService
import com.locate.locate.utility.CommonUtils
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Exception
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@Service
class LocationTraceServiceImpl():LocationTraceService {

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var commonJsonUtils: CommonUtils

    @Autowired
    lateinit var locationDao : LocationDAO

    override fun getStations(place:String): JSONObject {
        var resultJson = JSONObject()
        resultJson =  locationDao.findLocation(place)
        if(resultJson.isNullOrEmpty()) {
            var position = getAltitudeAndLongitudeByPlace(place)
            if (!position.isEmplty()) {
                var executorService = Executors.newFixedThreadPool(3)
                var tasks =  ArrayList<Callable<JSONObject>>()
                tasks.add( commonApiService(position, appProperties.parking))
                tasks.add(commonApiService(position, appProperties.charging))
                tasks.add(commonApiService(position, appProperties.eatDrink))
                var results = executorService.invokeAll(tasks)
                resultJson.put("Parking_Place", results.get(0).get())
                resultJson.put("Charging_Station", results.get(1).get())
                resultJson.put("Eat_Drink_Place", results.get(2).get())
                locationDao.setLocation(place, resultJson)
                executorService.shutdown();
            } else
                resultJson.put("Description", "No result found for this location")
        }
        return resultJson;
    }

    fun commonApiService(position: Position, category: String): Callable<JSONObject>{
        val callable = object : Callable<JSONObject> {
            override fun call(): JSONObject {
                var restTemplate = RestTemplate()
                var headers =  HttpHeaders()
                headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
                var builder = UriComponentsBuilder.fromHttpUrl(appProperties.searchCategs)
                        .queryParam("at",position.altitude+","+position.longitude)
                        .queryParam("cat",category)
                        .queryParam("apiKey",appProperties.apiKey)
                var entity = HttpEntity<String>(headers)
                var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String::class.java)
                var  result1 = result.body as String
                var parser =  JSONParser()
                var jsonObject = parser.parse(result1) as JSONObject
                return  commonJsonUtils.parseJsonResponse(jsonObject)
            }
        }
        return callable
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
