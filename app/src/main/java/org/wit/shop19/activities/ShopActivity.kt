package org.wit.shop19.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import org.wit.shop19.R



class ShopActivity : AppCompatActivity() {
    var myImageButton: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        myImageButton = findViewById<View>(R.id.my_image_button) as ImageButton
        myImageButton!!.setOnClickListener {
            val intentLoadNewActivity =
                Intent(this@ShopActivity, PlacemarkListActivity::class.java)
            startActivity(intentLoadNewActivity)
        }
    }
}