package asc.tool.passwordforgotunlocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("ASC", "got event");
		context.startService(new Intent(context, PasswordForgotUnlockerService.class));
	}
}
