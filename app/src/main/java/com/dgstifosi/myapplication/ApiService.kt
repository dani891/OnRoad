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


    }data class RouteResponse(
        val routes: List<Route>
    )

    data class Route(
        val geometry: Geometry
    )

    data class Geometry(
        val coordinates: List<List<Double>> // Lista de coordenadas [longitud, latitud]
    )
