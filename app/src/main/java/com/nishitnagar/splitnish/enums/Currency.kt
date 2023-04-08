package com.nishitnagar.splitnish.enums

import com.nishitnagar.splitnish.data.exception.DataException

enum class Currency(val code: String, val symbol: String) {
    INR("INR", "\u20B9");

    companion object {
        fun getByCode(code: String): Currency {
            return values().firstOrNull { currency -> currency.code == code }
                ?: throw DataException("Currency with code \"$code\" not found")
        }
    }
}