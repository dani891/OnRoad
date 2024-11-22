package com.dgstifosi.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Navegador : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private lateinit var map: GoogleMap


    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navegador)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    val botonRuta = findViewById<Button>(R.id.btnRuta)

    // Configurar el botón para ejecutar la ruta al hacer clic
    botonRuta.setOnClickListener {
        val startLocation = LatLng(-34.0, 151.0)  // Coordenadas de inicio
        val endLocation = LatLng(-33.8, 151.2)    // Coordenadas de fin
        createRoute(startLocation, endLocation)    // Crear la ruta
    }
}
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        enableLocation()
        // Agregar un marcador en la ubicación inicial (ejemplo con coordenadas)
        val initialLocation = LatLng(-34.0, 151.0)  // Puedes reemplazar estas coordenadas por la ubicación del usuario
        map.addMarker(MarkerOptions().position(initialLocation).title("Marker in Madrid"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        val startLocation = LatLng(-34.0, 151.0)  // Coordenadas de inicio
        val endLocation = LatLng(-33.8, 151.2)    // Coordenadas de fin
        createRoute(startLocation, endLocation)
    }

    private fun createRoute(start: LatLng, end: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            // Realizamos la llamada a OpenRouteService usando Retrofit
            val call = getRetrofit().create(OpenRouteServiceAPI::class.java)
                .getRoute("5b3ce3597851110001cf6248aee410f149cf4b11991009f068b39401", "${start.longitude},${start.latitude}", "${end.longitude},${end.latitude}")

            if (call.isSuccessful) {
                val routeResponse = call.body()
                val route = routeResponse?.routes?.get(0)
                val coordinates = route?.geometry?.coordinates

                // Dibujar la ruta en el mapa
                val polylineOptions = PolylineOptions()
                coordinates?.forEach {
                    polylineOptions.add(LatLng(it[1], it[0]))  // Los datos vienen como [longitud, latitud]
                }
                runOnUiThread {
                    map.addPolyline(polylineOptions)
                }
            } else {
                // Manejar el error de la API
                runOnUiThread {
                    Toast.makeText(this@Navegador, "Error al obtener la ruta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // Verifica si el permiso de ubicación ha sido otorgado
    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    // Habilitar la ubicación en el mapa
    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos de la aplicación", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    // Método para manejar los resultados de la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos de la aplicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método cuando se hace clic en el botón de mi ubicación
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    // Función para obtener el Retrofit con la API base URL
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/") // URL base de la API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}