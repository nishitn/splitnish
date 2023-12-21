package com.nishitnagar.splitnish.util

import androidx.room.TypeConverter
import com.nishitnagar.splitnish.enums.Currency
import java.time.LocalDateTime

class RoomTypeConvertors {
  @TypeConverter
  fun toCurrency(value: String) = enumValueOf<Currency>(value)

  @TypeConverter
  fun fromCurrency(value: Currency) = value.name

  @TypeConverter
  fun toLocalDateTime(dateTimeString: String): LocalDateTime = LocalDateTime.parse(dateTimeString)

  @TypeConverter
  fun fromLocalDateTime(dateTime: LocalDateTime): String = dateTime.toString()
}