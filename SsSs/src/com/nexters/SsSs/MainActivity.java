package com.nexters.ssss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout; //drawer영역
	ImageView hamburger; //햄버거아이콘
	ExpandableListView expListView;//햄버거바 전체영역
	List<String> listDataHeader; //상위루트명
	HashMap<String, List<String>> listDataChild;//하위루트명
	ExpandableListAdapter listAdapter; //햄버거바 전체영역어댑터
	Fragment diaryFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hamburger = (ImageView)findViewById(R.id.hamburger);//햄버거바 아이콘
		hamburger.setOnClickListener(hamburgerOnclickListener);//햄버거바 클릭시
		setUpDrawer();
	}
	
	//햄버거바에서 클릭이벤트를 했을때 drawer창 열닫기 이벤트
	View.OnClickListener hamburgerOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mDrawerLayout.isDrawerOpen(expListView)){
				mDrawerLayout.closeDrawer(expListView);
			}else{
				mDrawerLayout.openDrawer(expListView);
			}
		}
	};
	
	/**
	 * 햄버거바에 메뉴들을 세팅한다.
	 */
	private void setUpDrawer(){
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(mDrawerListener);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		//햄버거바 데이터 세팅
		prepareListData();
		//상위루트->하위루트 형식가진 어댑터 생성
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		//햄버거바화면에 어댑터 붙히기
		expListView.setAdapter(listAdapter);
		//하위루트(diary)눌렀을때 화면구성객체생성
		diaryFragment = new DiaryFragment();
		//메인중앙화면에 fragment와 트렌젝션하여 중앙화면에 이벤트처리
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, diaryFragment).commit();
		mDrawerLayout.closeDrawer(expListView);
		
		//햄버거바 하위루트클릭했을때 이벤트처리
		expListView.setOnChildClickListener(new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent,
		View v, int groupPosition, int childPosition, long id) {
			switch (groupPosition) {
			case 1:
				switch (childPosition) {
				case 0:
					diaryFragment = new DiaryFragment();
					Toast.makeText(getApplicationContext(), "diaryfragment화면", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
				break;
			
			}
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, diaryFragment).commit();
		mDrawerLayout.closeDrawer(expListView);
		return false;
		}
					
		});
	}
/*	
	//햄버거바 상위루트 클릭했을때 이벤트
	private OnItemClickListener mDrawerItemClickedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch(position){
			case 1:
				diaryFragment = new DiaryFragment();
				break;
			default:
				break;
			}
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, diaryFragment).commit();
			mDrawerLayout.closeDrawer(expListView);
		}
			
	};*/
		
	//DrawerLinstener 세팅.
	private DrawerListener mDrawerListener = new DrawerListener() {

		@Override
		public void onDrawerClosed(View arg0) {
			
		}

		@Override
		public void onDrawerOpened(View arg0) {
			
		}

		@Override
		public void onDrawerSlide(View arg0, float arg1) {
			
		}

		@Override
		public void onDrawerStateChanged(int arg0) {
			
		}
		
	};
	
	/**
	 * 상위,하위루트 데이터 세팅
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// 상위루트 세팅
		listDataHeader.add("게시판");
		listDataHeader.add("다이어리");
		listDataHeader.add("설정");

		// 하위루트 세팅
		List<String> noticeBoard = new ArrayList<String>();
		noticeBoard.add("(미정)게시물보기");
		noticeBoard.add("(미정)인기순보기");
		List<String> kakaoDiary = new ArrayList<String>();
		kakaoDiary.add("카카오톡 대화목록");
		kakaoDiary.add("(미정)저장된 대화내용보기");

		List<String> kakaoSetting = new ArrayList<String>();
		kakaoSetting.add("(미정)프로필편집");
		kakaoSetting.add("(미정)비밀번호설정");

		listDataChild.put(listDataHeader.get(0), noticeBoard); // Header, Child data
		listDataChild.put(listDataHeader.get(1), kakaoDiary);
		listDataChild.put(listDataHeader.get(2), kakaoSetting);
	}
	
	/**
	 * 상위루트->하위루트 형식가진 어댑터 생성
	 */
	public class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; //햄버거바의 상위루트
		private HashMap<String, List<String>> _listDataChild; //햄버거바의 하위루트
		public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData){
			this._context = context;
			this._listDataHeader = listDataHeader;
			this._listDataChild = listDataChild;
	}
		
	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}
		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	}
}
