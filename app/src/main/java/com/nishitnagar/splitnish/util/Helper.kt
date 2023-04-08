package com.nishitnagar.splitnish.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nishitnagar.splitnish.data.entity.UserEntity
import java.text.DecimalFormat

class Helper {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()

        var activeUserEntity: UserEntity? = null

        val decimalFormat = DecimalFormat("#.##")
    }
}