package gaia.uranus.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/7/21.
 */
public class SDCardUtils {

    static public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }
}
