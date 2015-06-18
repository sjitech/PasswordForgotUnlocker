package asc.tool.passwordforgotunlocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d("ASC", "got event");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				context.startService(new Intent(context, PasswordForgotUnlockerService.class));
			}
		}, 10 * 1000);
	}
}
