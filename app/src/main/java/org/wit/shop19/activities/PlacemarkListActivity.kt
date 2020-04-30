package org.wit.shop19.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_placemark_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.shop19.R
import org.wit.shop19.main.MainApp
import org.wit.shop19.models.PlacemarkModel



class PlacemarkListActivity : AppCompatActivity(), PlacemarkListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark_list)
        app = application as MainApp
        toolbar.title = title
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = PlacemarkAdapter(app.placemarks.findAll(), this)
        loadPlacemarks()
    }

    private fun loadPlacemarks() {
        showPlacemarks( app.placemarks.findAll())
    }

    fun showPlacemarks (placemarks: List<PlacemarkModel>) {
        recyclerView.adapter = PlacemarkAdapter(placemarks, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<TescoActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPlacemarkClick(placemark: PlacemarkModel) {
        startActivityForResult(intentFor<TescoActivity>().putExtra("placemark_edit", placemark), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadPlacemarks()
        super.onActivityResult(requestCode, resultCode, data)
    }
}