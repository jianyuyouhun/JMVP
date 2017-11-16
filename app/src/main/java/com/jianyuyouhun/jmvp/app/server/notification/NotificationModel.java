package com.jianyuyouhun.jmvp.app.server.notification;

import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public <T> void notifyNewMsg(T notice) {
        notifyNewMsg(notice, 5);
    }

    @SuppressWarnings("unchecked")
    public <T> void notifyNewMsg(T notice, int timeCount) {
        for (NotificationAction action : notificationActionList) {
            Class child = notice.getClass();
            Class<?>[] interfaces = action.getClass().getInterfaces();//获取实现的接口列表
            for (int i = 0; i < interfaces.length; i++) {
                Class actionClass = interfaces[i];
                if (actionClass == NotificationAction.class) {//找到NotificationAction接口实现
                    Type genericType = action.getClass().getGenericInterfaces()[i];//获取NotificationAction的参数泛型
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Class parent = (Class) parameterizedType.getActualTypeArguments()[0];
                    boolean isChild = parent.isAssignableFrom(child);//匹配参数相同的广播对应的接口实现
                    if (isChild) {
                        action.onNewMsg(notice, timeCount);
                    }
                    break;
                }
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
        void onNewMsg(T notice, int timeCount);
    }
}
