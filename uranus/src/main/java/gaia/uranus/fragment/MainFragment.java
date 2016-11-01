package gaia.uranus.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import gaia.uranus.R;
import gaia.uranus.base.ActionBaseSetting;
import gaia.uranus.core.ApiCore;
import gaia.uranus.pojo.MapMarker;
import gaia.uranus.utils.LogUtils;

/**
 * creator：OrandNot on 2016/2/15 09:30
 * email：zhoulianchun@foxmail.com
 */
public class MainFragment extends Fragment {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    // 定位相关
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private boolean isFirstLoc = true; // 是否首次定位
    private ArrayList<MapMarker> markList = new ArrayList<MapMarker>();
    private List<AVObject> friendsList = new ArrayList<AVObject>();
//    private View markView;
//    private TextView remark;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMapView = (TextureMapView) rootView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        initBroadcastReceive();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
//        test();
        if (AVUser.getCurrentUser() != null)
            getContactor();
        return rootView;
    }

    private void getContactor() {

        ApiCore.getContactorList(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    LogUtils.e("查询联系人有 " + avObjects.size() + " 条符合条件的数据");
                    friendsList =  avObjects;
                    for (AVObject obj : avObjects) {
                        LogUtils.e(">>>>>ContactorId>>>"+obj.get("contactor_ids").toString());
                        getContactorBehavors(obj);
                    }
                } else {
                    Log.e("失败", "查询错误: " + e.getMessage());
                }
            }
        });

    }

    private void getContactorBehavors(final AVObject obj1) {

        ApiCore.getContactorBehavors(obj1.get("contactor_ids").toString(),new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是符合条件的第一个 AVObject
                if (e == null ) {
                    if(avObject != null) {
                        LogUtils.e("Contactor"+obj1.get("contactor_ids").toString()+"最后"+avObject.get("createdAt")+"出现在:"+avObject.get("address"));
                        View markView =getActivity().getLayoutInflater().inflate(R.layout.marker_layout, null);
                        TextView remark=(TextView)markView.findViewById(R.id.remark);
                        if(obj1.get("contactor_remark")!=null)
                        remark.setText(obj1.get("contactor_remark").toString()+"");
                        bdA = BitmapDescriptorFactory.fromView(markView);
                        LatLng latlng = new LatLng(avObject.getDouble("latitude"), avObject.getDouble("longitude"));
                        mBaiduMap.addOverlay(new MarkerOptions().position(latlng).icon(bdA));
                    }else{
                        LogUtils.e("Contactor "+obj1.get("contactor_ids").toString()+" 没有数据");
                    }
                } else {
                    Log.e("失败", "查询错误: " + e.getMessage());
                }
            }
        });

    }

    private Bitmap getViewBitmap(View addViewContent) {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(broadcastReceiver);
    }


    private CoreBroadcastReceiver broadcastReceiver;

    private void initBroadcastReceive() {
        broadcastReceiver = new CoreBroadcastReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this.getActivity());
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ActionBaseSetting.LOCATION_ACTION);
        mIntentFilter.addAction(ActionBaseSetting.UPDATE_MAIN_ACTION);
        lbm.registerReceiver(broadcastReceiver, mIntentFilter);
    }

    private class CoreBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ActionBaseSetting.LOCATION_ACTION.equals(intent.getAction())) {
                BDLocation location = (BDLocation) intent.getExtras().get("location");
                if (location == null || mMapView == null) {
                    return;
                }
                LogUtils.d("onReceive Location>>>>>>>" + location.getLatitude() + " " + location.getLongitude() + " " + location.getAddress());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc && location.getAddress() != null) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    if (AVUser.getCurrentUser() != null)
                        ApiCore.upLoadLocation(location.getLatitude(), location.getLongitude(), location.getAddrStr());
                }
            }else if(ActionBaseSetting.UPDATE_MAIN_ACTION.equals(intent.getAction())){
                if (AVUser.getCurrentUser() != null) {
                    mBaiduMap.clear();
                    getContactor();
                }
            }
        }
    }
}
