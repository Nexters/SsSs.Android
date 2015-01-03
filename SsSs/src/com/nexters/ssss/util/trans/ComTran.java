package com.nexters.ssss.util.trans;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.nexters.ssss.util.conf.conf;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ComTran {
	/*
	 * 전문 코드 정의
	 */
	private static final String FIELD_JSON_SERVICE = "_req_svc";
	private static final String FIELD_JSON_REQUEST_DATA = "_req_data";
	private static final String FIELD_JSON_RESPONSE_DATA = "_res_data";
	private static final String FIELD_TEST_TRANS = "IS_TEST";
	
	/*
	 * 송수신 함수 및 I/O 변수 정의
	 */
	private static final int CONNECTION_TIMEOUT = 30000;
	private static String strJSONOutput = null;
	private static String strJSONInput = null;
	private static JSONObject joResData;
	
	/*
	 * 오류 메시지 정의
	 */
	private static String strAlertErrCd = ""; 
	private static String strAlertErrMessage = ""; 
	private static String strAlertErrAction = ""; 
	
	private static String ERRCD_UNKNOWNHOST = "ComTran_ERR0002";
	private static String ERRCD_CONVERTSERVERMSG = "ComTran_ERR0003";
	private static String ERRCD_CONVERTCLIENTMSG = "ComTran_ERR0001";
	
	private static String ERRMSG_UNKNOWNHOST = "서버와 연결할 수 없습니다.";
	private static String ERRMSG_CONVERTSERVERMSG = "수신된 데이터를 변환할 수 없습니다.";
	private static String ERRMSG_CONVERTCLIENTMSG = "송신할 데이터를 변환할 수 없습니다.";
	
	private static String ERRACT_APPKILL = "appkill";
	private static String ERRACT_FINISH = "finish";

	private Activity mActivity;
	
	private DefaultHttpClient httpclient = null;
	
	public ComTran (Activity mac){
		mActivity = mac;
	}
	
	public ComTran () {
		
	}
	
	/**
	 * 전문을 송신 및 수신합니다.
	 * @param strSvcCd 호출할 전문 코드
	 * @param mapReqData 서버에 보낼 데이터
	 * @return
	 */
	public JSONObject sendSvc(String strSvcCd, Map<String, Object> mapReqData) {
		try {
			makeJsonData(strSvcCd, mapReqData);
			sendData();
			parseJsonData();
		} catch (Exception e)  {
			mActivity.runOnUiThread( new Runnable() {
				public void run() {
					try {
						//오류메시지를 띄운다.
						showAlertDialog(strAlertErrCd, strAlertErrMessage, strAlertErrAction);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		
		return joResData;
	}
	
	/**
	 * 서버에서 수신받은 데이터를 파싱시킵니다.
	 * @throws Exception
	 */
	private void parseJsonData() throws Exception {
		JSONObject joParseData;
		JSONArray jaResList;
		try {
			joParseData = new JSONObject(strJSONInput);
			jaResList = new JSONArray(joParseData.getString(FIELD_JSON_RESPONSE_DATA));
			joResData = jaResList.getJSONObject(0);
			
			Log.d("trans", joResData.toString());
		} catch(Exception e) {
			strAlertErrCd = ERRCD_CONVERTSERVERMSG;
			strAlertErrMessage =  ERRMSG_CONVERTSERVERMSG;
			strAlertErrAction = ERRACT_FINISH;
			Log.d("trans", e.toString());
			throw e;
		}
	}
	
	/**
	 * 서버에 데이터를 전송합니다.
	 * @throws Exception
	 */
	private void sendData() throws Exception {
		HttpPost hpost = new HttpPost(conf.GATEWAY_URL);
		if(httpclient==null) {
			HttpParams httpparams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpparams, CONNECTION_TIMEOUT);
			
			httpclient = new DefaultHttpClient();
		}
		try {
			ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("JSONData", strJSONOutput));
			hpost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			
			ResponseHandler<String> reshand = new BasicResponseHandler();
			
			String strResponseBody = httpclient.execute(hpost, reshand);
			
			strJSONInput = strResponseBody.trim();
			strJSONOutput = null;
		}catch (Exception e) {
			strAlertErrCd = ERRCD_UNKNOWNHOST;
			strAlertErrMessage =  ERRMSG_UNKNOWNHOST;
			strAlertErrAction = ERRACT_FINISH;
			Log.d("trans", e.toString());
			throw e;
		}
		
	}
	
	/**
	 * 서버에 보낼 데이터를 만듭니다.
	 * @param strSvcCd 전문 서비스 코드
	 * @param mapReqData 송신 데이
	 * @throws JSONException
	 */
	private void makeJsonData(String strSvcCd, Map<String, Object> mapReqData) throws JSONException {
		JSONObject joReqData = new JSONObject(); //최종 JSON
		
		JSONObject joReqMap = new JSONObject(mapReqData); //Map -> JSONObject
		JSONArray jaReqData = new JSONArray();
		jaReqData.put(joReqMap);
		
		try {
			joReqData.put(FIELD_JSON_REQUEST_DATA, jaReqData);
			joReqData.put(FIELD_JSON_SERVICE, strSvcCd);
			
			//전문 전송 테스트이면 테스트 여부를 추가한다.
			if(conf.TRANS_TEST) {
				joReqData.put(FIELD_TEST_TRANS, true);
			}
		} catch (JSONException e) {
			strAlertErrCd = ERRCD_CONVERTCLIENTMSG;
			strAlertErrMessage =  ERRMSG_CONVERTCLIENTMSG;
			strAlertErrAction = ERRACT_FINISH;
			Log.d("trans", e.toString());
			
			throw e;
		}
		
		strJSONOutput = joReqData.toString();
	}
	
	/**
	 * 다이얼로그를 띄웁니다.
	 * @param errcd 타이틀
	 * @param errmsg 내용
	 * @param actioncd 액션코드
	 */
	public void showAlertDialog(String errcd, String errmsg, final String actioncd) {
		strAlertErrCd = errcd;
		strAlertErrMessage = errmsg;
		strAlertErrAction = actioncd;

		new AlertDialog.Builder(mActivity)
		.setTitle(strAlertErrCd)
		.setMessage(strAlertErrMessage)   
		.setNeutralButton(android.R.string.ok,   
				new DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if(strAlertErrAction.equals("finish"))
				{
					mActivity.finish();
				}

				if(strAlertErrAction.equals("appkill"))
				{
					ComUtil.applicationKill(mActivity);
				}
			}
		})
		.show();
	}
}
