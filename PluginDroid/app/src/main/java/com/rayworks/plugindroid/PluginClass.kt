package com.rayworks.plugindroid

import com.rayworks.commlib.Comm

// com.rayworks.plugindroid.PluginClass
class PluginClass : Comm {
    init {
        println(">>> Plugin Client initialized")
    }

    override fun add(p0: Int, p1: Int): Int = p0 + p1
}