package com.example.freelanceflow.util


import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object ModelUtils {

    private val gsonForSerialization: Gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriSerializer())
        .create()

    val gsonForDeserialization: Gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriDeserializer())
        .create()

     const val PREF_NAME = "models"

    fun save(context: Context, key: String, `object`: Any) {
        val sp: SharedPreferences = context.applicationContext.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE
        )

        val jsonString = gsonForSerialization.toJson(`object`)
        sp.edit().putString(key, jsonString).apply()
    }

    inline fun <reified T> read(context: Context, key: String, typeToken: TypeToken<T>): T? {
        val sp: SharedPreferences = context.applicationContext.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE
        )
        return try {
            gsonForDeserialization.fromJson(sp.getString(key, ""), typeToken.type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }


    private class UriSerializer : JsonSerializer<Uri> {
        override fun serialize(src: Uri, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src.toString())
        }
    }

    private class UriDeserializer : JsonDeserializer<Uri> {
        override fun deserialize(src: JsonElement, srcType: Type, context: JsonDeserializationContext): Uri {
            return Uri.parse(src.asString)
        }
    }
    fun clear(context: Context, key: String) {
        val sp: SharedPreferences = context.applicationContext.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE
        )
        sp.edit().remove(key).apply()
    }
}
