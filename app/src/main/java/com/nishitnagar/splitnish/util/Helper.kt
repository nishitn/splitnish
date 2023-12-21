package com.nishitnagar.splitnish.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nishitnagar.splitnish.data.entity.UserEntity
import java.io.Serializable
import java.text.DecimalFormat

class Helper {
  companion object {
    val gson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()

    var activeUserEntity: UserEntity? = null

    val decimalFormat = DecimalFormat("#.##")

//        fun getCategoryText(categoryId: UUID, allCategories: State<List<Category>>): String {
//            val categoryNames = mutableListOf<String>()
//            var currentId: UUID? = categoryId
//            while (currentId != null) {
//                val currentUnit = allCategories.value.firstOrNull { it.categoryEntity.id == currentId }
//                    ?: throw DataException("")
//                categoryNames.add(0, currentUnit.categoryEntity.label)
//                currentId = currentUnit.categoryEntity.parentCategoryId
//            }
//            return StringUtils.join(categoryNames, " - ")
//        }

    fun getKeyFromSerializedMap(
      entityData: Serializable?,
      key: String,
    ): String? {
      return if (entityData != null) {
        val map = entityData as HashMap<String, String>
        map[key]
      } else null
    }
  }
}