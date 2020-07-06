package com.locate.locate.service.serviceImpl

import com.locate.locate.service.ParkingService
import org.apache.tomcat.util.codec.binary.Base64
import org.json.simple.JSONObject
import org.springframework.http.*
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
class ParkingServiceImpl:ParkingService {

    override fun getParkingChargingStations(): ResponseEntity<JSONObject> {

//        var obj = JSONObject()
//        obj.put("token", result)
        //obj.put("first", "test for json object working")
        return getParkingPlace();
    }

    fun getParkingPlace(): ResponseEntity<JSONObject> {
         var url = "https://places.ls.hereapi.com/places/v1/discover/explore"
         var restTemplate = RestTemplate()
         var headers =  HttpHeaders()
         headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("at","52.5159,13.3777")
                .queryParam("cat","parking-facility")
                .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
         var entity = HttpEntity<String>(headers)
         var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, JSONObject::class.java)
         return result;
    }

     fun getChargingStations(): ResponseEntity<JSONObject> {
         var url = "https://places.ls.hereapi.com/places/v1/discover/explore"
         var restTemplate = RestTemplate()
         var headers =  HttpHeaders()
         headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
         var builder = UriComponentsBuilder.fromHttpUrl(url)
                 .queryParam("at","52.5159,13.3777")
                 .queryParam("cat","ev-charging-station")
                 .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
         var entity = HttpEntity<String>(headers)
         var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, JSONObject::class.java)
         return result;
    }

    fun getEatDrinkPlace(): ResponseEntity<JSONObject> {
        var url = "https://places.ls.hereapi.com/places/v1/discover/explore"
        var restTemplate = RestTemplate()
        var headers =  HttpHeaders()
        headers.accept = Arrays.asList(MediaType.APPLICATION_JSON);
        var builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("at","52.5159,13.3777")
                .queryParam("cat","ev-charging-station")
                .queryParam("apiKey","IMzzbZdRwUBm6W4K6tN40FnYpug36io5mmISETVFFCo")
        var entity = HttpEntity<String>(headers)
        var result= restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, JSONObject::class.java)
        return result;
    }
}



