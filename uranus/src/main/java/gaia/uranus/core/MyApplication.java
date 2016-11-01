package gaia.uranus.core;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.avos.avoscloud.AVOSCloud;
import com.baidu.mapapi.SDKInitializer;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.analytics.MobclickAgent;

import gaia.uranus.BuildConfig;
import gaia.uranus.utils.UmengPushUtlis;

/**
 * creator：Zclent on 2016/2/15 09:30
 * email：zhoulianchun@foxmail.com
 */
public class MyApplication extends Application {

    private LocationService loactionService;


    @Override
    public void onCreate() {
        super.onCreate();
        bindLocationService();
        SDKInitializer.initialize(this);
        AVOSCloud.initialize(this, "vYMo40m1G5VGudPD2cpoYMWS-gzGzoHsz", "luHzmJ70MNuotcit8UTWpdMW");
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setDebugMode( BuildConfig.DEBUG );
        UmengPushUtlis.getInstance().init(this);
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    // Handles the connection between the service and activity
    private ServiceConnection _connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Received when the service unexpectedly disconnects
            loactionService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Called when the connection is made
            loactionService = ((LocationService.LocationServiceBinder) service).getService();
        }
    };


    public void bindLocationService() {
        Intent intent = new Intent(MyApplication.this, LocationService.class);
        startService(intent); // 如果先调用startService,则在多个服务绑定对象调用unbindService后服务仍不会被销毁
        bindService(intent, _connection, Context.BIND_AUTO_CREATE);
    }




}
