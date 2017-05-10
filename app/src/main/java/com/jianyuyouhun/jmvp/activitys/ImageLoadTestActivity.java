package com.jianyuyouhun.jmvp.activitys;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadOptions;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

/**
 * 图片加载测试
 * Created by wangyu on 2017/5/10.
 */

public class ImageLoadTestActivity extends BaseActivity {

    @FindViewById(R.id.imageView)
    private ImageView mImageView;

    private SdcardModel sdcardModel = JApp.getInstance().getJModel(SdcardModel.class);

    String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494412663941&di=1a6f92aa75f9f3a83a6ea50e15a7ed72&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F18%2F69%2F88%2F564ec350569c1_1024.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadImage();
    }

    private void loadImage() {
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(sdcardModel.getImgPath(url));
        builder.setUrl(url);
        builder.bindImageView(mImageView);
        ImageLoader.getInstance().loadImage(builder.build(), new ImageLoadListener.SimpleImageLoadListener(){
            @Override
            public void onLoadFailed(String reason) {
                super.onLoadFailed(reason);
            }

            @Override
            public void onLoadSuccessful(BitmapSource bitmapSource, Bitmap bitmap) {
                super.onLoadSuccessful(bitmapSource, bitmap);
                mImageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_load_test;
    }

}
