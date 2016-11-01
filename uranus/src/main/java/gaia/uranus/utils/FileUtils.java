package gaia.uranus.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/10.
 */
public class FileUtils {
    public static String getExternalStoragePath() {
        if (Environment.getExternalStorageState() == null || !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "UPMiss";
    }

    public static String getExternalStorageImagePath() {
        if (Environment.getExternalStorageState() == null || !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + File.separator + "UPMiss";
    }


    public static String saveBitmap(Bitmap bmp, String dirName, String fileName) {
        String extPath = getExternalStorageImagePath();
        if (extPath == null)
            return null;

        String dirPath = extPath;
        if (!TextUtils.isEmpty(dirName))
            dirPath = extPath + File.separator + dirName;

        File dir = new File(dirPath);
        if (!dir.isDirectory() && !dir.mkdirs()) {
            return null;
        }

        FileOutputStream fos = null;
        try {
            File file = new File(dirPath, fileName);
            if (!file.exists() && !file.createNewFile()) {
                return null;
            }
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }
}
