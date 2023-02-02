package com.nishitnagar.splitnish

import androidx.room.TypeConverter
import com.nishitnagar.splitnish.data.enums.Currency
import java.time.LocalDateTime

class Convertors {
    @TypeConverter
    fun toCurrency(value: String) = enumValueOf<Currency>(value)

    @TypeConverter
    fun fromCurrency(value: Currency) = value.name

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): LocalDateTime = LocalDateTime.parse(dateTimeString)

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String = dateTime.toString()
}