package org.wit.shop19.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.shop19.R
import org.wit.shop19.models.Location
import org.wit.shop19.models.Location2
import org.wit.shop19.models.PlacemarkModel

class MapsActivity : AppCompatActivity(),  OnMapReadyCallback,  GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    ///// create location varible
    var location = Location()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        location = intent.extras?.getParcelable<Location>("location")!!


     /////////////// use of the fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(location.lat, location.lng)




        // add a red point marker
        val options = MarkerOptions()
            .title("Location")

            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)


        map.addMarker(options)
        //Add 2 more markers
        map.addMarker(options)
        map.addMarker(options)
//////// set the markers to listen to dragging
        map.setOnMarkerDragListener(this)
        ////////move the camara during dragging
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))

        map.setOnMarkerClickListener(this)
    }



//////// start the draging and connect it to the marker
    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom

    }




    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        val loc = LatLng(location.lat, location.lng)

        marker.setSnippet("GPS : " + loc.toString())

        return false
    }

}