package com.locate.locate.dao

import org.json.simple.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LocationDAO{
    @Autowired
    lateinit var  cache : CacheHelper
    fun findLocation(place : String): JSONObject{
        var result  = JSONObject()
        if (cache.getMapDetailsCacheObj().containsKey(place)) {
            return cache.getMapDetailsCacheObj().get(place);
        }
        return result
    }
    fun setLocation(place : String, locationDetails : JSONObject){
        if(!cache.getMapDetailsCacheObj().containsKey(place)){
            cache.getMapDetailsCacheObj().put(place, locationDetails);
        }
    }
}