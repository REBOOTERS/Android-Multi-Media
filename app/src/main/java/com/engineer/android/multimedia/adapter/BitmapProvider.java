package com.engineer.android.multimedia.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import heart.fun.creator.IProviderExpand;

/**
 * @author rookie
 * @since 09-18-2019
 */
public class BitmapProvider implements IProviderExpand<Bitmap> {
    private final List<String> list;
    private int index = 0;

    private Queue<byte[]> queue;

    private int[] scaleSize;

    public BitmapProvider(Activity activity, List<String> list) {
        this.list = list;
        this.scaleSize = getSize(activity);
        queue = new LinkedList<>();

        for (int i = 0; i < list.size(); i++) {
            Bitmap bitmap = genBitmap(i);
            queue.add(BitmapUtil.getBytesByPNG(bitmap));
            bitmap.recycle();
        }
    }

    public int[] getSize(Activity activity) {
        Display d = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        int widthPixs = metrics.widthPixels;
        int heightPixs = metrics.heightPixels;

        try {
            // 14 <= SDK_INT < 17
            widthPixs = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            heightPixs = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
        } catch (Exception e) {
        }

        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
            widthPixs = realSize.x;
            heightPixs = realSize.y;
        } catch (Exception e) {
        }

        return new int[]{widthPixs, heightPixs};
    }


    @Override
    public void prepare() {
    }

    @Override
    public void finish() {
    }

    @Override
    public void finishItem(Bitmap item) {
//        item.recycle();
//        System.gc();
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public int size() {
        return list.size();
    }

    private Bitmap genBitmap(int index) {
        String path = list.get(index);
        return BitmapUtil.decodeSampleBitmapFromResource(path, scaleSize[0], scaleSize[1]);
    }

    @Override
    public Bitmap next() {
        byte[] bytes = queue.poll();
        index++;
        return BitmapUtil.loadFromBytesByPNG(bytes);
    }
}
