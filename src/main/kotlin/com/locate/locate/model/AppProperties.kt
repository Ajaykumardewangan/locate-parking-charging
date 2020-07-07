package com.locate.locate.model

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${com.here.account.apiKey}")
    lateinit var apiKey: String

    @Value("\${com.here.cat.charging}")
    lateinit var charging: String

    @Value("\${com.here.cat.parking}")
    lateinit var parking: String

    @Value("\${com.here.cat.eatdrink}")
    lateinit var eatDrink: String

    @Value("\${com.here.url.searchplace}")
    lateinit var searchPlace: String

    @Value("\${com.here.url.searchcategs}")
    lateinit var searchCategs: String
}