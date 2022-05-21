package com.cybergod.oyeetaxi.api.model.userPaginated


import com.google.gson.annotations.SerializedName

data class Sort(
    @SerializedName("empty")
    var empty: Boolean,
    @SerializedName("sorted")
    var sorted: Boolean,
    @SerializedName("unsorted")
    var unsorted: Boolean
)