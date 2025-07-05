package com.hook2book.hbsync.UtilityClass

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject


class CustomeGsonAdapter {
    fun serialize(objects: List<Any?>, arrKey: String?, objKey: String?): String {
        val ja = JsonArray()
        for (`object` in objects) {
            val gson = Gson()
            val je = gson.toJsonTree(`object`)
            val jo = JsonObject()
            jo.add(objKey, je)
            ja.add(jo)
        }
        val objMain = JsonObject()
        objMain.add(arrKey, ja)
        return objMain.toString()
    }
}