package com.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.KClass

interface Json

private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()!!

fun <T: Json> T.serialize(kClass: KClass<T>) = moshi.adapter(kClass.java).toJson(this)

fun <T: Json> String.deserialize(kClass: KClass<T>) = moshi.adapter(kClass.java).fromJson(this)