package gaia.uranus.utils;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.format.Formatter;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by OrandNot on 2016/7/21.
 */
public class SystemInfoUtils {


    /**
     * 获取设备唯一id 方法一
     * bug:在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID
     * 需要READ_PHONE_STATE权限
     * 当设备被wipe后该数改变
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }



    /**
     * 获取设备唯一id 方法二
     * 安装后的唯一ID（即Installtion ID）
     * @param context
     * @return
     */
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";
    public synchronized static String getInstallationId(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }


    /**
     *获取当前app的签名信息
     * @param context
     * @return
     */
    public static String getSignature(Context context){
        PackageManager pm = context.getPackageManager();
        String pkgName = context.getPackageName();
        try {
            PackageInfo pi = pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            if(signatures != null && signatures.length > 0){
                String signature =  signatures[0].toCharsString();
//                Log.i(TAG, "Signature of current package is : " + signature);
                return  signature;
            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    /**
     * 获取apk文件的权限信息
     * @param apkFile
     * @param pm
     * @return
     */
    public static List<PermissionInfo> getApkPermissions(String apkFile, PackageManager pm){
        List<PermissionInfo> permissionInfos = new ArrayList<PermissionInfo>();
        PackageInfo packageInfo =  pm.getPackageArchiveInfo(apkFile, PackageManager.GET_PERMISSIONS);
        String[] permissions = packageInfo.requestedPermissions;
        for(String permName : permissions ){
            try {
                PermissionInfo permissionInfo = pm.getPermissionInfo(permName, 0);
                permissionInfos.add(permissionInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return permissionInfos;
    }


    /**
     * 得到当前系统内运行的app进程数量
     * @param context
     * @return
     */
    public static int getRunningAppProcesses(Context context) {

        ActivityManager am = (ActivityManager) context

                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appInfos = am.getRunningAppProcesses();

        return appInfos.size();

    }


    /**
     * 得到非系统应用的app信息
     * @param context
     * @return
     */
    public static List<HashMap<String, Object>> getAppInfo(Context context) {
        List<HashMap<String, Object>> appInfos = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);// 参数传入0表示过滤权限，因为可能有的手机不让你获取应用信息
        for (PackageInfo temp : packages) {
            if ((temp.applicationInfo.flags & temp.applicationInfo.FLAG_SYSTEM) == 0) {// 非系统应用
                String appName = temp.applicationInfo.loadLabel(
                        context.getPackageManager()).toString();//得到应用的名称
                Drawable appIcon = temp.applicationInfo.loadIcon(context
                        .getPackageManager());// 得到应用图标
                long lastUpdateTime = temp.lastUpdateTime;// 得到应用最后一次更新的时间
                String packageName = temp.packageName;// 得到应用的包名
                String versionName = temp.versionName;// 得到应用的版本信息
                map = new HashMap<String, Object>();
                map.put("appName", appName);
                map.put("appIcon", appIcon);
                map.put("lastUpdateTime", lastUpdateTime);
                map.put("packageName", packageName);
                map.put("versionName", versionName);
                appInfos.add(map);// 把应用信息加入集合中返回
                map = null;
            } else {
                // 系统应用
            }
        }
        return appInfos;
    }



    /**
     * 获取sd卡容量
     * @param context
     * @return
     */
    public static String getSdSize(Context context) {
        String totalStr = null, availStr = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 判断是否检测到sd卡
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                long blockSize = stat.getBlockSizeLong();
                long availableBlocks = stat.getAvailableBlocksLong();
                long totalBlocks = stat.getBlockCountLong();
                long totalSize = blockSize * totalBlocks;
                long availSize = blockSize * availableBlocks;
                totalStr = Formatter.formatFileSize(context, totalSize);
                availStr = Formatter.formatFileSize(context, availSize);
            }else{
                Toast.makeText(context, "该系统版本不支持获取sd卡容量", Toast.LENGTH_SHORT)
                        .show();
            }
        } else
            Toast.makeText(context, "没有检测到SD卡，请检查是否正确插入", Toast.LENGTH_SHORT)
                    .show();
        return "SD卡总容量为:" + totalStr + "可用为:" + availStr;
    }


    /**
     * 获取手机内存
     * @param context
     * @return
     */
    public static String getRomSave(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String totalStr = null, availStr = null;
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());// 得到手机内存的路径
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            long totalBlocks = stat.getBlockCountLong();
            long totalSize = blockSize * totalBlocks;
            long availSize = blockSize * availableBlocks;
            totalStr = Formatter.formatFileSize(context, totalSize);
            availStr = Formatter.formatFileSize(context, availSize);
            return "手机内存总大小为:" + totalStr + "可用空间为:" + availStr;
        }else{
            return "该系统版本不支持获取手机内存容量";
        }
    }

    public static void main(String[] args){
        System.out.println(""+StringUtils.isEmpty("hehe"));
    }

}
