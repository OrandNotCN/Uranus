package gaia.uranus.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动Service
 */
public class UranusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//此处不需要做任何操作，当开机启动时，应用会接受广播，并自动启动Application实施绑定Service  暂留以后做其他可能的处理
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

		}else if(Intent.ACTION_BATTERY_LOW.equals(intent.getAction())){

		}else if(Intent.ACTION_DATE_CHANGED.equals(intent.getAction())){

		}else if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){

		}else if(Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){

		}else if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){

		}else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){

		}else if(Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())){

		}

		Intent bindIntent = new Intent(context, LocationService.class);
		bindIntent.putExtra("action",intent.getAction());
		context.startService(bindIntent);
	}

}
