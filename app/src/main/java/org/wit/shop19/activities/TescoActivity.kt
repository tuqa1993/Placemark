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
import org.wit.shop19.helpers.readImageFromPath
import org.wit.shop19.helpers.showImagePicker
import org.wit.shop19.main.MainApp
import org.wit.shop19.models.Location
import org.wit.shop19.models.PlacemarkModel

class TescoActivity : AppCompatActivity(), AnkoLogger {

    var placemark = PlacemarkModel()
    var edit = false
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    //var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tesco)

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Placemark Activity started..")

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        app = application as MainApp

//    toolbar.title = title
//    setSupportActionBar(toolbar)

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            btnAdd.setText(R.string.save_placemark)
            placemark = intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")!!
            placemarkTitle.setText(placemark.title)
            description.setText(placemark.description)
            placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))

            if (placemark.image != null) {
                chooseImage.setText(R.string.change_placemark_image)
            }
        }

        btnAdd.setOnClickListener() {
            placemark.title = placemarkTitle.text.toString()
            placemark.description = description.text.toString()
            if (placemark.title.isEmpty()) {
                toast(R.string.enter_placemark_title)
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
            val location = Location(52.245696, -7.139102, 15f)
            if (placemark.zoom != 0f) {
                location.lat =  placemark.lat
                location.lng = placemark.lng
                location.zoom = placemark.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.placemark_menu, menu)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    placemark.image = data.getData().toString()
                    placemarkImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_placemark_image)
                }
            }
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

