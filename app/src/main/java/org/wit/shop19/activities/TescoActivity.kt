package org.wit.shop19.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_tesco.*
import kotlinx.android.synthetic.main.activity_placemark_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.shop19.R
import org.wit.shop19.helpers.readImage
import org.wit.shop19.helpers.showImagePicker
import org.wit.shop19.helpers.readImage
import org.wit.shop19.helpers.readImageFromPath
import org.wit.shop19.helpers.showImagePicker
import org.wit.shop19.main.MainApp
import org.wit.shop19.models.Location
import org.wit.shop19.models.Location2
import org.wit.shop19.models.PlacemarkModel

class TescoActivity : AppCompatActivity(), AnkoLogger {

    var placemark = PlacemarkModel()
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    //var location = Location(52.245696, -7.139102, 15f)
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tesco)

        info("Placemark Activity started..")

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        app = application as MainApp

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")!!
            placemarkTitle.setText(placemark.title)
            description.setText(placemark.description)
            placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
            btnAdd.setText(R.string.save_placemark)
        }
///////////// set click listener for the feilds
        btnAdd.setOnClickListener() {
            placemark.title = placemarkTitle.text.toString()
            placemark.description = description.text.toString()
            if (placemark.title.isEmpty()) {
                /////// just changed the string into eter your name
                toast(R.string.enter_your_name_title)
            } else {
                if (edit) {
                    app.placemarks.update(placemark.copy())
                } else {
                    app.placemarks.create(placemark.copy())
                }
            }
            info("add Button Pressed: $placemarkTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        placemarkLocation.setOnClickListener {
///// give the locations info intreduced in model values
            val location = Location(52.250210, -7.136998, 15f)


            if (placemark.zoom != 0f) {
                location.lat = placemark.lat
                location.lng = placemark.lng
                location.zoom = placemark.zoom
            }//////// mark the marker with location name to be showed
            startActivityForResult(
                intentFor<MapsActivity>().putExtra("location", location),
                LOCATION_REQUEST
            )

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                app.placemarks.delete(placemark)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
////// reuest an image to uplode
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    placemark.image = data.getData().toString()
                    placemarkImage.setImageBitmap(readImage(this, resultCode, data))
                    //Change the change place mark image to list image from strings
                    chooseImage.setText(R.string.change_list_image)
                }
            }//////////////////request location to upload
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")
                    if (location != null) {
                        placemark.lat = location.lat
                        placemark.lng = location.lng
                        placemark.zoom = location.zoom
                    }
                }
            }

        }
    }
}


