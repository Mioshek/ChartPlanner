package com.mioshek.chartplanner

import com.mioshek.chartplanner.assets.formats.DateFormatter
import org.junit.Assert
import org.junit.Test

class DateTests {
    @Test
    fun transformCESTtoUTCTest(){
        //08.05.2024 12:00 Timezone GTM + 2
        val dateCEST = 1715169600000L
        //08.05.2024 12:00 Timezone GTM
        val dateUTC = 1715162400000L
       Assert.assertEquals(dateUTC, DateFormatter.changeTimezone(dateCEST, false))
    }
}