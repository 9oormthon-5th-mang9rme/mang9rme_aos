package com.goormthon.mang9rme.kimbsu.common.util

import com.goormthon.mang9rme.common.data.StoneData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParserUtil {

    // region Base Function
    fun getString(jsonObj: JSONObject, key: String, strDefault: String = "") =
        if (jsonObj.has(key) && !jsonObj.isNull(key)) jsonObj.getString(key)
        else strDefault

    fun getBoolean(jsonObj: JSONObject, key: String, default: Boolean = false): Boolean {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            when (value.toLowerCase()) {
                "yes",
                "true",
                "y",
                "1" -> true
                else -> false
            }
        } else {
            default
        }
    }

    fun getInt(jsonObj: JSONObject, key: String, intDefault: Int = -1): Int {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                intDefault
            }
        } else {
            intDefault
        }
    }

    fun getFloat(jsonObj: JSONObject, key: String, floatDefault: Float = -1f): Float {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toFloat()
            } catch (e: NumberFormatException) {
                floatDefault
            }
        } else {
            floatDefault
        }
    }

    fun getDouble(jsonObj: JSONObject, key: String, doubleDefault: Double = -1.0): Double {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toDouble()
            } catch (e: NumberFormatException) {
                doubleDefault
            }
        } else {
            doubleDefault
        }
    }

    fun getJsonObject(jsonObject: JSONObject, key: String): JSONObject? {
        return if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            try {
                jsonObject.getJSONObject(key)
            } catch (e: JSONException) {
                null
            }
        } else {
            null
        }
    }

    fun getJSONArray(jsonObject: JSONObject, key: String): JSONArray? {
        return if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            try {
                jsonObject.getJSONArray(key)
            } catch (e: JSONException) {
                null
            }
        } else {
            null
        }
    }
    // endregion Base Function

    fun getStoneDataList(jsonRoot: JSONObject): ArrayList<StoneData> {
        val dataArray = getJSONArray(jsonRoot, "data")

        val stoneDataList = arrayListOf<StoneData>()
        if (dataArray != null) {
            for (idx in 0 until dataArray.length()) {
                if (!dataArray.isNull(idx)) {
                    val obj = dataArray.getJSONObject(idx)

                    val stoneId = getInt(obj, "stoneId")
                    val stoneName = getString(obj, "stoneName")
                    val stoneType = getString(obj, "stoneType")
                    val dateTime = getString(obj, "dateTime")
                    val address = getString(obj, "address")
                    val imgUrl = getString(obj, "imageUrl")
                    val rarity = getString(obj, "rarity")
                    val attack = getInt(obj, "attack")
                    val defense = getInt(obj, "defense")
                    val magicDefense = getInt(obj, "magicDefense")
                    val lat = getString(obj, "lat")
                    val lng = getString(obj, "lng")
                    val level = getString(obj, "level")

                    val stoneData = StoneData(
                        stoneId,
                        imgUrl,
                        stoneName,
                        stoneType,
                        dateTime,
                        address,
                        lat,
                        lng,
                        level,
                        rarity,
                        attack,
                        defense,
                        magicDefense
                    )
                    stoneDataList.add(stoneData)
                }
            }
        }

        return stoneDataList
    }

    fun getAddressData(jsonRoot: JSONObject): String {
        var ret = ""

        val metaObj = getJsonObject(jsonRoot, "meta")
        if (metaObj != null) {
            val totalCnt = getInt(metaObj, "total_count")

            if (totalCnt >= 1) {
                val documentsArray = getJSONArray(jsonRoot, "documents")
                if (documentsArray != null) {
                    if (documentsArray.length() > 0 && !documentsArray.isNull(0)) {
                        val obj = documentsArray.getJSONObject(0)

                        val roadAddrObj = getJsonObject(obj, "road_address")
                        if (roadAddrObj != null) {
                            val depth1 = getString(roadAddrObj, "region_2depth_name")
                            val depth2 = getString(roadAddrObj, "region_3depth_name")
                            ret = "$depth1 $depth2"
                        }

                        if (ret.isEmpty()) {
                            val addrObj = getJsonObject(obj, "address")
                            if (addrObj != null) {
                                val depth1 = getString(addrObj, "region_2depth_name")
                                val depth2 = getString(addrObj, "region_3depth_name")
                                ret = "$depth1 $depth2"
                            }
                        }
                    }
                }
            }
        }

        return ret
    }

    companion object {
        private const val TAG: String = "JsonParserUtil"
    }

}