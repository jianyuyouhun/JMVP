package com.jianyuyouhun.jmvp.ui.activitys;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.ui.adapter.TestBannerAdapter;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadOptions;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.jianyuyouhun.jmvplib.mvp.model.permission.OnRequestPermissionResultListener;
import com.jianyuyouhun.jmvplib.view.CircleImageView;
import com.jianyuyouhun.library.AutoBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片加载测试
 * Created by wangyu on 2017/5/10.
 */

public class ImageLoadTestActivity extends BaseActivity {

    @FindViewById(R.id.imageView)
    private CircleImageView mImageView;

    @FindViewById(R.id.autobanner)
    private AutoBannerView autoBannerView;

    @Model
    private SdcardModel sdcardModel;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private TestBannerAdapter adapter;

    String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494412663941&di=1a6f92aa75f9f3a83a6ea50e15a7ed72&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F18%2F69%2F88%2F564ec350569c1_1024.jpg";

    private OnRequestPermissionResultListener onRequestPermissionResultListener = new OnRequestPermissionResultListener() {
        @Override
        public void onRequestSuccess(String permission, String permissionName, boolean isNecessary) {
            go();
        }

        @Override
        public void onRequestFailed(String permission, String permissionName, boolean isNecessary) {
            showToast("你没有获取" + permissionName + "权限");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester.setOnRequestPermissionResultListener(onRequestPermissionResultListener);
        adapter = new TestBannerAdapter(getContext());
        autoBannerView.setAdapter(adapter);
        permissionRequester.requestPermission(this, "存储", Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void go() {
        loadImage();
        loadBanner();
    }

    private void loadImage() {
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(sdcardModel.getImgPath(url));
        builder.setUrl(url);
        builder.bindImageView(mImageView);
        imageLoader.loadImage(builder.build(), new ImageLoadListener.SimpleImageLoadListener(){
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

    private void loadBanner() {
        List<String> list = new ArrayList<>();
        list.add("http://img3.imgtn.bdimg.com/it/u=1749061261,2462112140&fm=21&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1949438216,3782070973&fm=23&gp=0.jpg");
        list.add("http://imgs.91danji.com/Upload/201419/2014191037351347278.jpg");
        list.add("http://img.newyx.net/newspic/image/201410/14/90ea74d5b0.jpg");
        adapter.changeItems(list);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_load_test;
    }

}
