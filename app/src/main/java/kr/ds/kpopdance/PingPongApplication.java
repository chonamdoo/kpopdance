package kr.ds.kpopdance;

import android.support.multidex.MultiDexApplication;

import kr.ds.config.Config;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;
import kr.ds.utils.UniqueID;


public class PingPongApplication extends MultiDexApplication {
	/**
	 * 이미지 로더, 이미지 캐시, 요청 큐를 초기화한다.
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		if(DsObjectUtils.isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.ANDROID_ID))){
			SharedPreference.putSharedPreference(getApplicationContext(), Config.ANDROID_ID, UniqueID.getUniqueID());
		}

		if(DsObjectUtils.isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.PREFERENCE_NEW))){
			SharedPreference.putSharedPreference(getApplicationContext(), Config.PREFERENCE_NEW, "ok");
			SharedPreference.putSharedPreference(getApplicationContext(), Config.YOUTUBE_AUTO_PLAY, true);
		}
	}
}
