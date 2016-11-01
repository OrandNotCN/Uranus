package gaia.uranus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class PreferenceUtil {

	/**
	 * @param context
	 * @param name 数据文件名字
	 * @param mode
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPrefString(Context context, String name, int mode, String key, final String defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(name, mode);
		return settings.getString(key, defaultValue);
	}

	public static void setPrefString(Context context, String name, int mode, final String key, final String value) {
		final SharedPreferences settings = context.getSharedPreferences(name, mode);
		settings.edit().putString(key, value).commit();
	}

	public static boolean getPrefBoolean(Context context, String name, int mode, final String key, final boolean defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(name, mode);
		return settings.getBoolean(key, defaultValue);
	}

	public static void setPrefBoolean(Context context, String name, int mode, final String key, final boolean value) {
		final SharedPreferences settings = context.getSharedPreferences(name, mode);
		settings.edit().putBoolean(key, value).commit();
	}

	public static void setPrefInt(Context context, String name, int mode, final String key, final int value) {
		final SharedPreferences settings = context.getSharedPreferences(name, mode);
		settings.edit().putInt(key, value).commit();
	}

	public static int getPrefInt(Context context, String name, int mode, final String key, final int defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(name, mode);
		return settings.getInt(key, defaultValue);
	}

	public static void remove(Context context, String name, int mode, final String key) {
		final SharedPreferences sp = context.getSharedPreferences(name, mode);
		final Editor editor = sp.edit();
		editor.remove(key).commit();
	}



	public static void clearPreference(Context context, String name, int mode) {
		final SharedPreferences sp = context.getSharedPreferences(name, mode);
		final Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}


	public static boolean isContainKey(Context context, String name, String key, int mode) {
		final SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 *
	 * @param context
	 * @param name
	 * @param mode
	 * @return
	 */
	public static Map<String, ?> getAll(Context context, String name, int mode) {
		SharedPreferences sp = context.getSharedPreferences(name,mode);
		return sp.getAll();
	}


	/**
	 * http://www.68idc.cn/help/buildlang/ask/20150723458340.html
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		private static Method findApplyMethod() {
			try {
				Class clz = Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		public static void apply(Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}
}
