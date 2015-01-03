package com.nexters.ssss.util.trans;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.nexters.Main.MainActivity;

public class ComUtil {
	/**
	 * 어떤 액티비티에서든지 해당 함수를 사용하면 앱을 강제 종료합니다.
	 * @param Activity 앱을 종료 시킬 액티비티를 보냅니다.
	 */
	public static void applicationKill (Activity mActivity){
		int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		Log.d("ComUtil", ""+sdkVersion);
		if(sdkVersion < 8) {
			ActivityManager am = (ActivityManager) mActivity.getSystemService(mActivity.ACCESSIBILITY_SERVICE);
			am.restartPackage(mActivity.getPackageName());
		} else {
			Intent intent = new Intent(mActivity, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("killapp", "true");
			mActivity.finish();
			
			Class<?> cls = mActivity.getClass();
			if(!cls.equals(MainActivity.class)) {
				mActivity.startActivity(intent);
			} else {
				//((MainActivity)mActivity).requestKillProcess();
			}
		}
	}
	
	/**
	 * 현재 연결된 네트워크 정보 확
	 * @param ctx 컨텍스트를 넘겨줍니다.
	 * @return str 현재 접속된 네트워크 종류를 String으로 넘겨줍니다.
	 */
	public String getNetworkType(Context ctx){
		ConnectivityManager cm =(ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni =cm.getActiveNetworkInfo();
        return ni.getTypeName();
	}
	
	/**
	 * 채널에 연결되있는지 확인합니다.
	 * @param ctx 컨텍스트를 넘겨 줍니다.
	 * @param channel 연결 하고자하는 채널을 적어줍니다. 
	 * @return boolean 확인하고자 하는 채널이 연결되있는지 확인합니다.
	 */
	public static boolean isConn(Context ctx, String channel){
		NetworkInfo ni = null;
		ConnectivityManager cm =(ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if("wifi".equals(channel))
			ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		else if("mobile".equals(channel))
			ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		return ni.isConnected();
	}
}
