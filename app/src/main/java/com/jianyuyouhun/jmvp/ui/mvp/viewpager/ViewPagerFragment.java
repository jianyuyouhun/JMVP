package com.jianyuyouhun.jmvp.ui.mvp.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnLongClick;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.app.BaseFragment;
import com.jianyuyouhun.jmvplib.mvp.model.SdcardModel;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadListener;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoadOptions;
import com.jianyuyouhun.jmvplib.utils.imageloader.ImageLoader;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;
import com.jianyuyouhun.jmvplib.view.adapter.MultiItem;
import com.jianyuyouhun.jmvplib.view.adapter.MultiItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagerFragment.ItemId.TYPE_ONE;
import static com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagerFragment.ItemId.TYPE_TREE;
import static com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagerFragment.ItemId.TYPE_TWO;

/**
 * Created by wangyu on 2017/9/11.
 */

public class ViewPagerFragment extends BaseFragment {

    private static final String KEY_TITLE = "title";

    private String mTitleString;

    @FindViewById(R.id.fragment_title)
    private TextView mTitle;

    @FindViewById(R.id.list_view)
    private ListView mListView;

    private MyAdapter adapter;

    private List<String> list = new ArrayList<>();
    private String[] urls = new String[]{
            "http://img3.imgtn.bdimg.com/it/u=1749061261,2462112140&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1949438216,3782070973&fm=23&gp=0.jpg",
            "http://imgs.91danji.com/Upload/201419/2014191037351347278.jpg",
            "http://img.newyx.net/newspic/image/201410/14/90ea74d5b0.jpg"
    };

    /**
     * use {@link #newInstance(String)} please
     */
    @Deprecated
    public ViewPagerFragment() {
    }

    public String getTitleString() {
        return mTitleString;
    }

    public static ViewPagerFragment newInstance(String title) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle argument = new Bundle();
        argument.putString(KEY_TITLE, title);
        viewPagerFragment.setArguments(argument);
        return viewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleString = getArguments().getString(KEY_TITLE, "");
        adapter = new MyAdapter(getContext());
        adapter.setData(buildData());
    }

    private List<MultiItem> buildData() {
        List<MultiItem> multiItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * 10) % 3;
            if (random == 0) {
                Item1 item1 = new Item1();
                int pos = (int) (Math.random() * 10) % 4;
                switch (pos) {
                    case 0:
                        item1.setName("艾希" + i);
                        break;
                    case 1:
                        item1.setName("猴子" + i);
                        break;
                    case 2:
                        item1.setName("瑞文" + i);
                        break;
                    case 3:
                        item1.setName("剑圣" + i);
                        break;
                }
                item1.setUrl(urls[pos]);
                multiItems.add(item1);
            } else if (random == 1) {
                Item2 item2 = new Item2();
                item2.setName("我的消息" + i);
                multiItems.add(item2);
            } else {
                Item3 item3 = new Item3();
                item3.setName("标题" + i);
                multiItems.add(item3);
            }
        }
        return multiItems;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_view_pager;
    }

    @Override
    protected void onCreateView(View rootView, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        mTitle.setText(mTitleString);
        mListView.setAdapter(adapter);
    }

    @OnLongClick(R.id.fragment_title)
    private boolean onTitleClick(View view) {
        getBaseActivity().showToast(mTitleString);
        return true;
    }

    public static class MyAdapter extends MultiItemAdapter {

        private ImageLoader imageLoader = ImageLoader.getInstance();

        @Model
        private SdcardModel sdcardModel;

        public MyAdapter(Context context) {
            super(context);
            ModelInjector.injectModel(this);
        }

        @Override
        protected void initItemType(List<Integer> typeList) {
            typeList.add(ItemId.TYPE_ONE);
            typeList.add(ItemId.TYPE_TWO);
            typeList.add(ItemId.TYPE_TREE);
        }

        @Override
        protected Class<? extends ViewHolder> getViewHolderByItemType(Integer itemType) {
            switch (itemType) {
                case ItemId.TYPE_ONE:
                    return ViewHolder1.class;
                case ItemId.TYPE_TWO:
                    return ViewHolder2.class;
                case ItemId.TYPE_TREE:
                    return ViewHolder3.class;
                default:
                    throw new RuntimeException("未解析的itemViewHolder, itemType: " + itemType);
            }
        }

        @Override
        protected void bindView(int itemType, ViewHolder viewHolder, MultiItem multiItem, int position) {
            switch (itemType) {
                case ItemId.TYPE_ONE:
                    ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
                    Item1 item1 = (Item1) multiItem;
                    bindItem1View(viewHolder1, item1, position);
                    break;
                case ItemId.TYPE_TWO:
                    ViewHolder2 viewHolder2 = (ViewHolder2) viewHolder;
                    Item2 item2 = (Item2) multiItem;
                    bindItem2View(viewHolder2, item2, position);
                    break;
                case ItemId.TYPE_TREE:
                    ViewHolder3 viewHolder3 = (ViewHolder3) viewHolder;
                    Item3 item3 = (Item3) multiItem;
                    bindItem3View(viewHolder3, item3, position);
                    break;
            }
        }

        private void bindItem1View(ViewHolder1 viewHolder, Item1 item, int position) {
            viewHolder.mTextView.setText(item.getName());
            final String url = item.getUrl();
            viewHolder.imageView.setTag(url);
            ImageLoadOptions.Builder builder = new ImageLoadOptions.Builder(sdcardModel.getImgPath(url));
            builder.setUrl(url);
            builder.bindImageView(viewHolder.imageView);
            final ImageView finalImageView = viewHolder.imageView;
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
        }

        private void bindItem2View(ViewHolder2 viewHolder2, Item2 item2, int position) {
            viewHolder2.mTextView.setText(item2.getName());
        }

        private void bindItem3View(ViewHolder3 viewHolder3, Item3 item3, int position) {
            viewHolder3.mTextView.setText(item3.getName());
        }

        public static class ViewHolder1 extends ViewHolder {

            @FindViewById(R.id.list_item_text1)
            private TextView mTextView;

            @FindViewById(R.id.list_image)
            private ImageView imageView;

            @Override
            protected int getLayoutId() {
                return R.layout.adapter_multi_item_1;
            }
        }

        public static class ViewHolder2 extends ViewHolder {

            @FindViewById(R.id.list_item_text2)
            private TextView mTextView;

            @Override
            protected int getLayoutId() {
                return R.layout.adapter_multi_item_2;
            }
        }

        public static class ViewHolder3 extends ViewHolder {

            @FindViewById(R.id.list_item_text3)
            private TextView mTextView;

            @Override
            protected int getLayoutId() {
                return R.layout.adapter_multi_item_3;
            }
        }
    }

    public class Item1 implements MultiItem {

        private String name;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int getItemType() {
            return ItemId.TYPE_ONE;
        }
    }

    public class Item2 implements MultiItem {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int getItemType() {
            return ItemId.TYPE_TWO;
        }
    }

    public class Item3 implements MultiItem {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int getItemType() {
            return ItemId.TYPE_TREE;
        }
    }

    @IntDef({TYPE_ONE, TYPE_TWO, TYPE_TREE})
    @interface ItemId {
        int TYPE_ONE = -1;
        int TYPE_TWO = -2;
        int TYPE_TREE = -3;
    }

}
