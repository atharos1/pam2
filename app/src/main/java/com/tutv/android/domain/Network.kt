package com.tutv.android.domain

data class Network(var id: Int, var name: String) {
    override fun toString(): String {
        return name
    }
}