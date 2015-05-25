package asc.tool.passwordforgotunlocker;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class PasswordForgotUnlockerService extends Service {
	boolean did_addGhostView;
	WindowManager wm;
	View ghostView;
	LayoutParams lp;

	boolean did_acquireWakeLock;
	PowerManager powerManager;
	PowerManager.WakeLock wakeLock;

	boolean did_disableKeyguard;
	KeyguardManager keyGuardManager;
	KeyguardManager.KeyguardLock keyGuardLock;

	@Override
	public void onCreate() {
		super.onCreate();

		// trick to adjust process oom_adj to 2 (hard to be killed when in low
		// memory)
		startForeground(Process.myPid(), new Notification());

		new Handler().postDelayed(new Runnable() {
			public void run() {
				turnOn();
			}
		}, 0);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				turnOn();
			}
		}, 5 * 1000);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				turnOn();
			}
		}, 10 * 1000);
	}

	void turnOn() {
		if (did_addGhostView) {
			wm.removeView(ghostView);
			did_addGhostView = false;
		}
		if (did_acquireWakeLock) {
			wakeLock.release();
			did_acquireWakeLock = false;
		}
		if (did_disableKeyguard) {
			keyGuardLock.reenableKeyguard();
			did_disableKeyguard = false;
		}

		if (wm == null)
			wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		if (ghostView == null)
			ghostView = new View(this);
		if (lp == null) {
			lp = new LayoutParams();
			lp.type = LayoutParams.TYPE_SYSTEM_ERROR;
			lp.width = 0;
			lp.height = 0;
			lp.flags = 0;
			lp.flags |= LayoutParams.FLAG_NOT_FOCUSABLE;
			lp.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
			lp.flags |= LayoutParams.FLAG_SHOW_WHEN_LOCKED;
			lp.flags |= LayoutParams.FLAG_TURN_SCREEN_ON;
			lp.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
			lp.flags |= LayoutParams.FLAG_DISMISS_KEYGUARD;
		}

		wm.addView(ghostView, lp);
		did_addGhostView = true;

		if (powerManager == null)
			powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		if (wakeLock == null)
			wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getPackageName() + '#' + Thread.currentThread().getId());
		wakeLock.acquire();
		did_acquireWakeLock = true;

		if (keyGuardManager == null)
			keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		if (keyGuardLock == null)
			keyGuardLock = keyGuardManager.newKeyguardLock(getPackageName() + '#' + Thread.currentThread().getId());
		keyGuardLock.disableKeyguard();
		did_disableKeyguard = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.exit(1);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
