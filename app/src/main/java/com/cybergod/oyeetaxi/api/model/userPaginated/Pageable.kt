package com.cybergod.oyeetaxi.api.model.userPaginated


import com.google.gson.annotations.SerializedName

data class Pageable(
    @SerializedName("offset")
    var offset: Int,
    @SerializedName("pageNumber")
    var pageNumber: Int,
    @SerializedName("pageSize")
    var pageSize: Int,
    @SerializedName("paged")
    var paged: Boolean,
    @SerializedName("sort")
    var sort: Sort,
    @SerializedName("unpaged")
    var unpaged: Boolean
)