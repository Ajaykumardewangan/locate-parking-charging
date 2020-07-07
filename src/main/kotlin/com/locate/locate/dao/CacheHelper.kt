package com.locate.locate.dao

import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.json.simple.JSONObject
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class CacheHelper {
    var cacheManager: CacheManager = CacheManagerBuilder.newCacheManagerBuilder().build()
    lateinit var mapDetailsCache : Cache<String, JSONObject>
     init {
         cacheManager.init();
         mapDetailsCache = cacheManager
                .createCache("MapCache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String::class.java,
                                JSONObject::class.java, ResourcePoolsBuilder.heap(100))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60*60*24))).build())
    }

    fun getMapDetailsCacheObj(): Cache<String, JSONObject>{
        return mapDetailsCache
    }
}