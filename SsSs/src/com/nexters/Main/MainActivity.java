package com.nexters.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nexters.SsSs.R;

public class MainActivity extends ActionBarActivity {
	private final String TAG = "MainActivity";
	TextView textView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 =(TextView)findViewById(R.id.textView1);
	}
	
	//sd카드내용읽기 버튼눌렀을때
	public void OnClickKakaoTalkReader(View v){
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/KakaoTalk/Chats";
		String folderName = null;
		String realFilePath = null;
		
		File talkFiles = new File(path);
		//Chats폴더가 있는지 없는지 검사를 위해
		File chatFiles = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/KakaoTalk");
		
		//KakaoTalk 폴더안에 Chats 파일이 있는지 검사
		if(chatFiles.listFiles().length !=3){
			Log.i(TAG, "Chats파일이 없습니다.");
			return ;
		}else if(talkFiles.listFiles().length >0){
			Log.i(TAG,"Chats파일 존재");
			//Chats파일이 존재한다면
			for(File file : talkFiles.listFiles()){
				folderName = file.getName(); //Chats폴더안에 ex)KakaTalk_Chats_2015-01-01_13.19.56 폴더이름
				String tmp = file.getName(); //이 폴더이름의 날짜를 구분하기위해서 tmp 만듬
				
				//저장 날짜 구별
				String saveYear = tmp.substring(16,20);
				String saveMonth = tmp.substring(21,23);
				String svaeDay = tmp.substring(24,26);
				
				//실제  최하위 폴더 path위치
				realFilePath = path +"/" + folderName;
				//최하위 폴더 안에 txt파일
				File txtFile = new File(realFilePath);
				for(File f : txtFile.listFiles()){
					if(f.getName().equals("KakaoTalkChats.txt")){
						Log.i(TAG, "kakaoTalkChats.txt 존재함");
						
						FileInputStream fis = null;
						BufferedReader bufferReader = null;
						
						try{
							fis = new FileInputStream(realFilePath + "/" + "KakaoTalkChats.txt");
							bufferReader = new BufferedReader(new InputStreamReader(fis));
							
							String str = null;
							
							while((str = bufferReader.readLine()) != null){
								Log.i(TAG, str);
								textView1.append(str);
								textView1.append("\n");
							}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}catch (IOException e) {
								e.printStackTrace();
							}
						
						}
					}
			}
			
		}
	
	}
	
	//sd카드내용삭제 버튼눌렀을때
	public void OnClickKakaoTalkDelete(View v){
		//textView 화면지우기
		textView1.setText(" ");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
