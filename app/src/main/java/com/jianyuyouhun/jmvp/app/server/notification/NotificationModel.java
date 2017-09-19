package com.jianyuyouhun.jmvp.app.server.notification;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wangyu on 2017/9/19.
 */

public class NotificationModel extends BaseJModel<App> {

    private List<NotificationAction> notificationActionList = new ArrayList<>();

    @Override
    public void onModelCreate(App app) {
        super.onModelCreate(app);
    }

    public void notifyNewMsg(Object notice) {
        notifyNewMsg(notice, 5);
    }

    @SuppressWarnings("unchecked")
    public void notifyNewMsg(Object notice, int timeCount) {
        for (NotificationAction action : notificationActionList) {
            Class child = notice.getClass();
            Class parent = action.returnNoticeType();
            boolean isChild = parent.isAssignableFrom(child);
            if (isChild) {
                action.onNewMsg(notice, timeCount);
            }
        }
    }

    public void registerNotificationAction(NotificationAction notificationAction) {
        notificationActionList.add(notificationAction);
    }

    public void unregisterNotificationAction(NotificationAction notificationAction) {
        notificationActionList.remove(notificationAction);
    }

    public interface NotificationAction<T> {
        void onNewMsg(T object, int timeCount);
        Class returnNoticeType();
    }
}
