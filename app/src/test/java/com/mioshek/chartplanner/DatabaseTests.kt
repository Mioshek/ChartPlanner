package com.mioshek.chartplanner

import org.junit.Test
import java.time.LocalDate


import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DatabaseTests {
    @Test
    fun plusYear() {
        val initialDate = LocalDate.of(2024, 1, 1)

        // Calculate the date 366 days later
        val resultDate = initialDate.plusDays(366)

        // Print the result
        assertEquals(resultDate, LocalDate.of(2025,1,1))
    }
}