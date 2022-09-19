package com.urcloset.smartangle.model


import com.google.gson.annotations.SerializedName

data class MapResponserModel(
    @SerializedName("plus_code")
    var plusCode: PlusCode? = null,
    @SerializedName("results")
    var results: List<Result?>? = null,
    @SerializedName("status")
    var status: String? = null
) {
    data class PlusCode(
        @SerializedName("compound_code")
        var compoundCode: String? = null,
        @SerializedName("global_code")
        var globalCode: String? = null
    )

    data class Result(
        @SerializedName("access_points")
        var accessPoints: List<Any?>? = null,
        @SerializedName("address_components")
        var addressComponents: List<AddressComponent?>? = null,
        @SerializedName("formatted_address")
        var formattedAddress: String? = null,
        @SerializedName("geometry")
        var geometry: Geometry? = null,
        @SerializedName("place_id")
        var placeId: String? = null,
        @SerializedName("types")
        var types: List<String?>? = null
    ) {
        data class AddressComponent(
            @SerializedName("long_name")
            var longName: String? = null,
            @SerializedName("short_name")
            var shortName: String? = null,
            @SerializedName("types")
            var types: List<String?>? = null
        )

        data class Geometry(
            @SerializedName("bounds")
            var bounds: Bounds? = null,
            @SerializedName("location")
            var location: Location? = null,
            @SerializedName("location_type")
            var locationType: String? = null,
            @SerializedName("viewport")
            var viewport: Viewport? = null
        ) {
            data class Bounds(
                @SerializedName("northeast")
                var northeast: Northeast? = null,
                @SerializedName("southwest")
                var southwest: Southwest? = null
            ) {
                data class Northeast(
                    @SerializedName("lat")
                    var lat: Double? = null,
                    @SerializedName("lng")
                    var lng: Double? = null
                )

                data class Southwest(
                    @SerializedName("lat")
                    var lat: Double? = null,
                    @SerializedName("lng")
                    var lng: Double? = null
                )
            }

            data class Location(
                @SerializedName("lat")
                var lat: Double? = null,
                @SerializedName("lng")
                var lng: Double? = null
            )

            data class Viewport(
                @SerializedName("northeast")
                var northeast: Northeast? = null,
                @SerializedName("southwest")
                var southwest: Southwest? = null
            ) {
                data class Northeast(
                    @SerializedName("lat")
                    var lat: Double? = null,
                    @SerializedName("lng")
                    var lng: Double? = null
                )

                data class Southwest(
                    @SerializedName("lat")
                    var lat: Double? = null,
                    @SerializedName("lng")
                    var lng: Double? = null
                )
            }
        }
    }
}