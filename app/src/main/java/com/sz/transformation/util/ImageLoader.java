package com.sz.transformation.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.sz.transformation.R;

/**
 * @author Sean Zhang
 * Date 2021/8/14
 */
public class ImageLoader {
    /**
     * 加载资源图片
     */
    public static void load(int drawRes, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(drawRes)
                .into(imageView);
    }

    /**
     * 加载资源图片圆角矩形
     */
    public static void load(String path, ImageView imageView, int roundRadius) {
        Glide.with(imageView.getContext())
                .load(path)
                .fallback(R.drawable.ic_head_photo)
                .override(150, 150)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(roundRadius)))
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param path
     * @param imageView
     */
    public static <T> void loadImage(T path, ImageView imageView, int roundRadius) {
        Glide.with(imageView.getContext())
                .load(path)
                .fallback(R.drawable.ic_head_photo)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(roundRadius)))
                .into(imageView);
    }
    public static <T> void loadImage(T path, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(path)
                .fallback(R.drawable.ic_head_photo)
                .into(imageView);
    }

    /**
     * 使用渐变效果加载图片
     * @param path
     * @param imageView
     * @param <T>
     */
    public static <T> void loadImageWithTransition(T path,ImageView imageView){
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        Glide.with(imageView.getContext())
                .load(path)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
}
