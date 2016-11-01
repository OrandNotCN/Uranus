package gaia.uranus.core;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import gaia.uranus.utils.LogUtils;

/**
 * creator：Zclent on 2016/8/1 17:34
 * email：zhoulianchun@foxmail.com
 */
public class ApiCore {


    public static void upLoadLocation(double latitude,double longitude,String address){
        AVUser user = AVUser.getCurrentUser();
        if(user==null){
            return ;}
        AVObject avObject = new AVObject("bl_location");
        avObject.put("user_ids",user.getObjectId());
        avObject.put("latitude",latitude);
        avObject.put("longitude", longitude);
        avObject.put("address", address);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e!=null)
                LogUtils.e("---------upLoadLocation-------"+e.getMessage());
            }
        });
    }

    public static void addNewFriends(String fid, SaveCallback callback) {
        AVUser user = AVUser.getCurrentUser();
        AVObject testObject = new AVObject("bl_contactor");
        testObject.put("user_ids",user.getObjectId());
        testObject.put("contactor_ids",fid);
        testObject.saveInBackground(callback);
    }



    public static void isHaveFriends(String fid,FindCallback<AVObject> callback){
        AVQuery<AVObject> query = new AVQuery<AVObject>("bl_contactor");
        query.whereEqualTo("user_ids",AVUser.getCurrentUser().getObjectId());
        query.whereEqualTo("contactor_ids",fid);
        query.findInBackground(callback);
    }

    public static void searchFriends(String phone,FindCallback<AVObject> callback){
        AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
        query.whereEqualTo("mobilePhoneNumber", phone);
        query.findInBackground(callback);
    }

    public static  void getContactorList( FindCallback<AVObject> callback) {
        AVQuery<AVObject> query = AVQuery.getQuery("bl_contactor");
        query.whereEqualTo("user_ids", AVUser.getCurrentUser().getObjectId());
        query.findInBackground(callback);

    }
    public static  void getContactorBehavors(String contactorId,GetCallback<AVObject> callback) {
        AVQuery<AVObject> query = new AVQuery<>("bl_location");
        query.whereEqualTo("user_ids",contactorId);
        query.order("createdAt");
        query.getFirstInBackground(callback);
    }
}
