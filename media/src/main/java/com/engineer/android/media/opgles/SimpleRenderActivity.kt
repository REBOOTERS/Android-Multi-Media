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
        viewBinding.glSurfaceView.setRenderer(SimpleRender(drawer))
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

class SimpleRender(private val drawer: IDrawer) : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        drawer.setTextureID(OpenGLTools.createTextureIds(1)[0])
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        drawer.draw()
    }

}

