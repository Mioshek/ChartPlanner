package com.mioshek.chartplanner.assets.formats

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Instant.ofEpochSecond
import java.time.ZoneId
import java.util.TimeZone

class DateFormatter {
    companion object{
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val timezoneOffset = getTimezoneOffsetInMilis()
        val currentTimeInSec = getCurrentTimeInSec()
        val currentDate = ((System.currentTimeMillis() + timezoneOffset) /86400000).toInt()

        /**
         * @param [date]: Long in seconds to be transformed into other timezone
         * @param [toUTC]: Boolean, determines whether change timezone to current or UTC
         * @return [date]: Long in milis of ready date
         */
        fun changeTimezone(date: Long, toUTC: Boolean): Long {
            val formattedDate = date * 1000

            return if (toUTC) formattedDate - timezoneOffset else formattedDate + timezoneOffset
        }

        /**
         * @param [date] in seconds (Epoch): Long
         * @return [date], transformed to starting hours of this day in seconds (Epoch): Long
         */
        fun getStartDayTime(date: Long): Long {
            val instant = ofEpochSecond(date)
            val zoneId = ZoneId.systemDefault()
            return instant.atZone(zoneId).toLocalDate().atStartOfDay(zoneId).toInstant().toEpochMilli() /1000
        }
    }

}

fun getTimezoneOffsetInMilis(): Int {
    val currentTimeZone = TimeZone.getDefault()
    val utcTimeZone = TimeZone.getTimeZone("UTC")
    val currentOffsetInMillis = currentTimeZone.getOffset(System.currentTimeMillis())
    val utcOffsetInMillis = utcTimeZone.getOffset(System.currentTimeMillis())

    // Calculate the offset in hours
    return (currentOffsetInMillis - utcOffsetInMillis)
}

fun getCurrentTimeInSec(): Long {
    return (System.currentTimeMillis() - getTimezoneOffsetInMilis()) / 1000
}