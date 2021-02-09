package com.engineer.android.multimedia

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.android.media.ui.SimpleCodecActivity
import com.engineer.android.media.ui.SimpleMediaPlayerActivity
import com.engineer.android.multimedia.ui.CreatorRootActivity
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        media.setOnClickListener {
            startActivity(Intent(this, SimpleMediaPlayerActivity::class.java))
        }

        decoder.setOnClickListener {
            startActivity(Intent(this, SimpleCodecActivity::class.java))
        }
        creator.setOnClickListener {
            startActivity(Intent(this, CreatorRootActivity::class.java))
        }

        requestPermission()
    }

    private fun requestPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request { _, _, _ -> }
    }
}
