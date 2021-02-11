package com.engineer.android.multimedia.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.engineer.android.multimedia.R
import com.engineer.android.multimedia.adapter.BitmapProvider
import com.engineer.android.multimedia.adapter.Glide4Engine
import com.engineer.android.multimedia.adapter.MergyHandler
import com.engineer.android.multimedia.databinding.ActivityCreatorRootBinding
import com.exozet.transcoder.mcvideoeditor.MediaCodecTranscoder
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import heart.`fun`.creator.task.AvcExecuteAsyncTask
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class CreatorRootActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCreatorRootBinding
    private var datas = ArrayList<Uri>()
    private var lists = ArrayList<String>()
    private lateinit var adapter: ImageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreatorRootBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        RxPermissions(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    Matisse.from(this)
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .showSingleMediaType(true)
                        .imageEngine(Glide4Engine())
                        .maxSelectable(9)
                        .forResult(100)
                }
            }
    }

    @SuppressLint("CheckResult")
    private fun go() {
        if (datas.isEmpty()) {
            return
        }
        viewBinding.progressViewCircle.visibility = View.VISIBLE
        val destPath = Environment.getExternalStorageDirectory()
            .absolutePath + File.separator + "testrere"
        val srcPath = getFileStreamPath(MP4_FILE).path

        val handler = MergyHandler(srcPath, "$destPath/$MP4_FILE")
        handler.setCallback(object : MergyHandler.Callback {
            override fun progress(progress: Int) {
                viewBinding.progressViewCircle.progress = progress.toFloat()
            }

            override fun start() {
                viewBinding.progressViewCircle.visibility = View.VISIBLE
            }

            override fun end(path: String?) {
                viewBinding.progressViewCircle.visibility = View.GONE
                viewBinding.jzVideo.visibility = View.VISIBLE
                viewBinding.jzVideo.setUp(path, "test")
            }

        })
        Observable.just("")
            .map {
                AvcExecuteAsyncTask.execute(BitmapProvider(this, lists), 1, handler, srcPath)
            }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            val uris = Matisse.obtainResult(data)
            val urls = Matisse.obtainPathResult(data)

            datas.clear()
            datas.addAll(uris)

            lists.clear()
            lists.addAll(urls)
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("CheckResult")
    private fun use_exozet() {

        val path = Environment
            .getExternalStorageDirectory()
            .absolutePath + File.separator + "aaaa/"

        var out = Environment
            .getExternalStorageDirectory()
            .absolutePath + File.separator + "aaaaa/test.mp4"

        val frameFolder = Uri.parse(path)
        val outputVideo = Uri.parse(out)

        MediaCodecTranscoder.createVideoFromFrames(
            frameFolder = frameFolder,
            outputUri = outputVideo,
            deleteFramesOnComplete = false
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("exo", "end==${it.duration}")
            }, {
                it.printStackTrace()
            })
    }


    // <editor-fold defaultstate="collapsed" desc="initView">
    private fun initView() {
        viewBinding.list.layoutManager = GridLayoutManager(this, 3)
        adapter = ImageListAdapter(datas)
        viewBinding.list.adapter = adapter
        viewBinding.get.setOnClickListener { loadData() }
        viewBinding.go.setOnClickListener { go() }
        viewBinding.exozet.setOnClickListener { use_exozet() }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="adapter">
    inner class ImageListAdapter(val datas: List<Uri>) :
        RecyclerView.Adapter<ImageListAdapter.MyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.image_card, parent, false)
            return MyHolder(view)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.image.setImageURI(datas[position])
        }


        inner class MyHolder(v: View) : RecyclerView.ViewHolder(v) {
            val image = v.findViewById<ImageView>(R.id.image)
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="static">
    companion object {
        val MP4_FILE = "test.mp4"
    }
    // </editor-fold>
}
