package com.engineer.android.media.opgles

import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.android.media.R
import com.engineer.android.media.databinding.ActivitySimpleRenderBinding
import com.engineer.android.media.opgles.drawer.BitmapDrawer
import com.engineer.android.media.opgles.drawer.IDrawer
import com.engineer.android.media.opgles.drawer.TriangleDrawer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SimpleRenderActivity : AppCompatActivity() {

    private lateinit var drawer: IDrawer

    private lateinit var viewBinding: ActivitySimpleRenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySimpleRenderBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        drawer = provideDrawer()
        initRender(drawer)
    }

    private fun initRender(drawer: IDrawer) {
        viewBinding.glSurfaceView.setEGLContextClientVersion(2)
        val render = SimpleRender()
        render.addDrawer(drawer)
        viewBinding.glSurfaceView.setRenderer(render)
    }

    override fun onDestroy() {
        super.onDestroy()
        drawer.release()
    }

    private fun provideDrawer(): IDrawer {
        val drawers = ArrayList<IDrawer>()
        drawers.add(TriangleDrawer())
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.boy)
        drawer = BitmapDrawer(bitmap)
        drawers.add(drawer)

        return drawers.random()
    }
}



