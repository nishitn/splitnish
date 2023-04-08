package com.nishitnagar.splitnish.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class DateTimeUtil {
    companion object {
        private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy")

        private val yearMonthFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

        private val timeFormatH12: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        private val timeFormatH24: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun formatDate(date: LocalDate): String {
            return dateFormat.format(date)
        }

        fun formatTime(time: LocalTime): String {
            return timeFormatH24.format(time)
        }

        fun formatYearMonth(yearMonth: YearMonth): String {
            return yearMonthFormat.format(yearMonth)
        }

        fun readDate(dateString: String): LocalDate {
            return LocalDate.parse(dateString, dateFormat)
        }

        fun readTime(timeString: String): LocalTime {
            return LocalTime.parse(timeString, timeFormatH24)
        }
    }
}