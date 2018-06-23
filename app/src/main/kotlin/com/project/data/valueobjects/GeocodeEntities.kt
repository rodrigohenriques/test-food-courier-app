package com.project.data.valueobjects

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    val status: String,
    val results: List<GeocodeResult>?
)

data class GeocodeResult(
    @SerializedName("address_components") val addressComponents: List<AddressComponent>,
    @SerializedName("formatted_address") val formattedAddress: String,
    val geometry: Geometry?,
    @SerializedName("partial_match") val partialMatch: Boolean,
    @SerializedName("place_id") val placeId: String,
    val types: List<String>
)

data class AddressComponent(
    @SerializedName("long_name") val longName: String,
    @SerializedName("short_name") val shortName: String,
    val types: List<String>
)

data class Geometry(
    val location: LatLng,
    @SerializedName("location_type") val locationType: String,
    val viewport: Viewport
)

data class Viewport(
    val northeast: LatLng,
    val southwest: LatLng
)
