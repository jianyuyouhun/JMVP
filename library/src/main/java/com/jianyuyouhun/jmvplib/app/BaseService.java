package com.jianyuyouhun.jmvplib.app;

import android.app.Service;
import android.os.Message;

import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.app.broadcast.MsgWhat;
import com.jianyuyouhun.jmvplib.app.broadcast.OnGlobalMsgReceiveListener;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;

/**
 * service 基类
 * Created by wangyu on 2017/7/5.
 */

public abstract class BaseService extends Service implements OnGlobalMsgReceiveListener {

    @Override
    public void onCreate() {
        super.onCreate();
        ModelInjector.injectModel(this);
        LightBroadcast.getInstance().addOnGlobalMsgReceiveListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LightBroadcast.getInstance().removeOnGlobalMsgReceiveListener(this);
    }

    @Override
    public void onReceiveGlobalMsg(Message msg) {
        if (msg.what == MsgWhat.ALL_SERVICE_STOP_YOUR_SELF.getValue()) {
            stopSelf();
        }
    }
}
