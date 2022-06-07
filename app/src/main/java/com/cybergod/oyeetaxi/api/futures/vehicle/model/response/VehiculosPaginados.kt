package com.cybergod.oyeetaxi.api.futures.vehicle.model.response


import com.cybergod.oyeetaxi.api.futures.share.model.pagination.Pageable
import com.cybergod.oyeetaxi.api.futures.share.model.pagination.Sort
import com.google.gson.annotations.SerializedName

data class VehiculosPaginados(
    @SerializedName("content")
    var content: List<VehiculoResponse>,
    @SerializedName("empty")
    var empty: Boolean,
    @SerializedName("first")
    var first: Boolean,
    @SerializedName("last")
    var last: Boolean,
    @SerializedName("number")
    var number: Int,
    @SerializedName("numberOfElements")
    var numberOfElements: Int,
    @SerializedName("pageable")
    var pageable: Pageable,
    @SerializedName("size")
    var size: Int,
    @SerializedName("sort")
    var sort: Sort,
    @SerializedName("totalElements")
    var totalElements: Int,
    @SerializedName("totalPages")
    var totalPages: Int
)