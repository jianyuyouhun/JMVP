package com.jianyuyouhun.jmvp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadOptions;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;
import com.jianyuyouhun.library.AutoBannerAdapter;

import java.util.List;

/**
 * 自动轮播图适配器
 * Created by wangyu on 2017/5/10.
 */

public class TestBannerAdapter extends AutoBannerAdapter {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<String> urls;
    private Context context;

    @Model
    private SdcardModel sdcardModel;

    public TestBannerAdapter(Context context) {
        this.context = context;
        ModelInjector.injectModel(this);
    }

    public void changeItems(@NonNull List<String> urls) {
        this.urls = urls;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    @Override
    protected Object getItem(int position) {
        return urls.get(position);
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
