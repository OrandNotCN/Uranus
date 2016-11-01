package gaia.uranus.utils;

import android.os.Environment;
import android.text.format.DateFormat;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import gaia.uranus.BuildConfig;


/**
 * Created by OrandNot on 2016/7/21.
 * 日志类，定位到具体类，具体行
 */
public final class LogUtils {
	private static boolean debug = BuildConfig.DEBUG;//打包改成false
	private static String TAG;

	public static boolean isDebug() {
		return debug;
	}

	public static void v(String msg) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.v(TAG, s);
		}
	}

	public static void v(Throwable e) {
		if (debug) {
			String s = buildMessage();
			android.util.Log.v(TAG, s, e);
		}
	}

	public static void v(String msg, Throwable e) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.v(TAG, s, e);
		}
	}

	public static void d(String msg) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.d(TAG, s);
		}
	}

	public static void d(Throwable e) {
		if (debug) {
			String s = buildMessage();
			android.util.Log.d(TAG, s, e);
		}
	}

	public static void d(String msg, Throwable e) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.d(TAG, s, e);
		}
	}

	public static void i(String msg) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.i(TAG, s);
			saveErrorLog("info:"+s);
		}
	}

	public static void i(String msg, Throwable e) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.i(TAG, s, e);
		}
	}

	public static void i(Throwable e) {
		if (debug) {
			String s = buildMessage();
			android.util.Log.i(TAG, s, e);
		}
	}

	public static void w(String msg) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.w(TAG, s);
		}
	}

	public static void w(Throwable e) {
		if (debug) {
			String s = buildMessage();
			android.util.Log.w(TAG, s, e);
		}
	}

	public static void w(String msg, Throwable e) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.w(TAG, s, e);
		}
	}

	public static void e(String msg) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.e(TAG, s);
			saveErrorLog("error:"+s);
		}
	}

	public static void e(Throwable e) {
		if (debug) {
			String s = buildMessage();
			android.util.Log.e(TAG, s, e);
			saveErrorLog("error:"+s);
		}
	}

	public static void e(String msg, Throwable e) {
		if (debug) {
			String s = buildMessage(msg);
			android.util.Log.e(TAG, s, e);
			saveErrorLog("error:"+s);
		}
	}

	protected static String buildMessage() {
		return buildMessage("");
	}

	protected static String buildMessage(String msg) {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
		TAG = caller.getClassName();
		return String.format(
				"[method:%s, lineNumber:%d]:%s",
				new Object[] { caller.getMethodName(), caller.getLineNumber(), msg });
	}
	
	public static void saveErrorLog(String msg) {
		String errorlog = "info.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mpocket/log/";
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//没有挂载SD卡，无法写文件
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("==="+DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance())+"===");	
			pw.println(msg);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pw != null){ pw.close(); } 
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}
	}
	static final String BOUNDARY = "0xKhTmLbOuNdArY";

	public static void uploadErrorLog() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String log = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kidcare/log/error.txt";
				File file = new File(log);
				if(!file.exists()) return;
				
				try {
					String urlStr = "服务器地址" + "/action/fileupload.action"; // servleturl

					HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("connection", "Keep-Alive");
					conn.setRequestProperty("user-agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
					conn.setRequestProperty("Charsert", "UTF-8");
					conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

					OutputStream out = new DataOutputStream(conn.getOutputStream());

					StringBuffer sb = new StringBuffer();
					sb.append("--" + BOUNDARY + "\r\n")
						.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n")
						.append("Content-Type: application/octet-stream\r\n")
						.append("Content-Transfer-Encoding: binary\r\n\r\n");

					out.write(sb.toString().getBytes());
					sb.delete(0, sb.length());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					
					out.write(("\r\n--" + BOUNDARY + "--").getBytes());
					in.close();
					out.flush();
					out.close();

					String jsonStr = inputStream2String(conn.getInputStream());
					LogUtils.i("jsonStr:" + jsonStr);
					JSONObject json = new JSONObject(jsonStr);
					boolean resule = json.getBoolean("result");
					LogUtils.d("json"+json);
					if(resule) {
						file.delete();
					}

				} catch (Exception e) {
					LogUtils.e(e);
				}
			}
		}).start();
	}

	public static String inputStream2String(InputStream is) {
		StringBuffer sb = new StringBuffer();
		byte[] byt = new byte[4096]; // 4k
		int i = -1;
		try {
			while ((i = is.read(byt)) != -1) {
				sb.append(new String(byt, 0, i));
			}
		} catch (IOException e) {
			LogUtils.e(e);
		}
		return sb.toString();
	}
}