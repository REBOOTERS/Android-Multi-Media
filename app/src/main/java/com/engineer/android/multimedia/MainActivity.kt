package com.engineer.android.multimedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.android.media.ui.SimpleMediaPlayerActivity
import com.engineer.android.multimedia.ui.CreatorRootActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        media.setOnClickListener {
            startActivity(Intent(this, SimpleMediaPlayerActivity::class.java))
        }
        creator.setOnClickListener {
            startActivity(Intent(this, CreatorRootActivity::class.java))
        }
    }
}
