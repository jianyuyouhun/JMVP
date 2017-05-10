package com.jianyuyouhun.jmvp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadOptions;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.library.AutoBannerView;

import java.util.List;

/**
 * 自动轮播图适配器
 * Created by wangyu on 2017/5/10.
 */

public class TestBannerAdapter implements AutoBannerView.AutoBannerAdapter {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<String> urls;
    private Context context;

    private SdcardModel sdcardModel = App.getApp().getJModel(SdcardModel.class);

    public TestBannerAdapter(Context context) {
        this.context = context;
    }

    public void changeItems(@NonNull List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    @Override
    public View getView(View convertView, int position) {
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        final String url = urls.get(position);
        imageView.setTag(url);
        ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(sdcardModel.getImgPath(url));
        builder.setUrl(urls.get(position));
        builder.bindImageView(imageView);
        final ImageView finalImageView = imageView;
        imageLoader.loadImage(builder.build(), new ImageLoadListener.SimpleImageLoadListener(){
            @Override
            public void onLoadFailed(String reason) {
                super.onLoadFailed(reason);
            }

            @Override
            public void onLoadSuccessful(BitmapSource bitmapSource, Bitmap bitmap) {
                super.onLoadSuccessful(bitmapSource, bitmap);
                if (finalImageView.getTag().toString().equals(url)) {
                    finalImageView.setImageBitmap(bitmap);
                }
            }
        });
        return finalImageView;
    }
}
