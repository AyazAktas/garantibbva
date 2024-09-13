package com.example.garantibbva.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.garantibbva.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.model.PlacesSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val apiKey = "a"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getUserLocation()
    }

    private fun getUserLocation() {
        val testLocation = LatLng(40.9265, 29.3111)
        mMap.addMarker(MarkerOptions().position(testLocation).title("Yenişehir Merkez"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 14F))
        showNearbyATMs(testLocation)
    }

    private fun showNearbyATMs(userLocation: LatLng) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val context = GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build()

                val response: PlacesSearchResponse = PlacesApi.nearbySearchQuery(context, com.google.maps.model.LatLng(userLocation.latitude, userLocation.longitude))
                    .radius(5000)
                    .keyword("Garanti")
                    .await()

                withContext(Dispatchers.Main) {
                    response.results.forEach { place ->
                        val placeLocation = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                        mMap.addMarker(MarkerOptions().position(placeLocation).title(place.name))
                    }
                }
            } catch (e: Exception) {
                Log.e("MapsFragment", "Error fetching places", e)
            }
        }
    }
}
