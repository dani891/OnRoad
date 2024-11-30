package com.dgstifosi.myapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteServiceAPI {
    @GET("v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<RouteResponse>
    // Método para la búsqueda de direcciones (Geocoding)
    @GET("geocode/search")
    suspend fun searchAddress(
        @Query("api_key") apiKey: String,
        @Query("text") query: String
    ): Response<AddressResponse>
    }

     data class AddressResponse(
     val features: List<AddressFeature>
     )

    data class AddressFeature(
    val geometry: Geometry,
    val properties: AddressProperties
    )

    data class AddressProperties(
    val label: String  // Nombre o etiqueta de la dirección encontrada
    )

    data class RouteResponse(
    val routes: List<Route>
    )

    data class Route(
    val geometry: Geometry
    )

    data class Geometry(
    val coordinates: List<List<Double>> // Lista de coordenadas [longitud, latitud]
    )
