package com.jianyuyouhun.inject;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

/**
 * View查找器
 * Created by wangyu on 2017/4/5.
 */

public abstract class ViewFinder {
    public static ViewFinder create(Activity activity) {
        return new ActivityViewFinder(activity);
    }

    public static ViewFinder create(View view) {
        return new ViewViewFinder(view);
    }

    public static ViewFinder create(Dialog dialog) {
        return new DialogViewFinder(dialog);
    }

    public abstract View bindView(int id);

    public static class ActivityViewFinder extends ViewFinder {
        private Activity activity;

        public ActivityViewFinder(Activity activity) {
            super();
            this.activity = activity;
        }

        @Override
        public View bindView(int id) {
            return activity.findViewById(id);
        }
    }

    public static class ViewViewFinder extends ViewFinder {
        private View view;

        public ViewViewFinder(View view) {
            super();
            this.view = view;
        }

        @Override
        public View bindView(int id) {
            return view.findViewById(id);
        }
    }

    public static class DialogViewFinder extends ViewFinder {
        private Dialog dialog;

        public DialogViewFinder(Dialog dialog) {
            super();
            this.dialog = dialog;
        }

        @Override
        public View bindView(int id) {
            return dialog.findViewById(id);
        }
    }
}
