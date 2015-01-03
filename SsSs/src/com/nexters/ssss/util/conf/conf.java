/**
 * 앱 설정 정보
 */
package com.nexters.ssss.util.conf;


public class conf {
	/**
	 * 배포 여부
	 */
	public static final boolean IS_RELEASE = false;
	
	/**
	 * 리얼,개발 구분
	 */
	public static final boolean IS_REAL = IS_RELEASE?true:false;
	
	/**
	 * 테스트 전문 전송 여부
	 */
	public static final boolean TRANS_TEST = IS_REAL?false:true;
	
	/**
	 * GATEWAY URL
	 */
	public static String GATEWAY_URL = IS_REAL?RealConf.GATEWAY_URL:DevConf.GATEWAY_URL;
	
	/**
	 * Facebook URL
	 */
	public static String FACEBOOK_URL = IS_REAL?RealConf.FACEBOOK_URL:DevConf.FACEBOOK_URL;
	
	/**
	 * 리얼 설정 정보
	 */
	private class RealConf {
		private static final String GATEWAY_URL = "http://ssss.maden.kr/gateway";
		private static final String FACEBOOK_URL = "http://www.facebook.com/pages/ssss";
	}
	
	/**
	 * 개발 설정 정보
	 */
	private class DevConf {
		private static final String GATEWAY_URL = "http://ssss.maden.kr/gateway";
		private static final String FACEBOOK_URL = "http://www.facebook.com/pages/ssss";
	}
}