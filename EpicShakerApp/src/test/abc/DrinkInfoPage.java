package test.abc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import test.abc.DrinkNameSearchPage.CData;
import test.abc.DrinkNameSearchPage.DataAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DrinkInfoPage extends Activity {
	
	private JSONObject jsonObj = null;
	private ListView listview;
	// 데이터를 연결할 Adapter
	DataAdapter  adapter;
	// 데이터를 담을 자료구조
	ArrayList<CData> alist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.drinkinfopage);
		String name = "";
		String jsonStr = "";
		String drinkId = "";
		Log.d("epic", jsonStr);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			jsonStr = extras.getString("JSONstr");
			drinkId = extras.getString("id");
			//Log.d("epic", jsonStr);
			try {
				jsonObj = new JSONObject(jsonStr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			name = jsonObj.getString("name");
			TextView nameView =(TextView)findViewById(R.id.textView3);
			nameView.setText(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 댓글 리스트 가져오기
		try {
			Method setListMethod;
			Class[] parameterTypes = new Class[1];
	        parameterTypes[0] = String.class;
			setListMethod = DrinkInfoPage.class.getMethod("setListData", parameterTypes);
			new HttpGetJson(setListMethod,DrinkInfoPage.this,"http://epicshakerprj.appspot.com/getpost/?beverage_id=" + drinkId);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Button btn1=(Button)findViewById(R.id.button3);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				sendDataToDrinkMake();
			}
		});
		
	}

	public void sendDataToDrinkMake() {		
		Intent intent = new Intent(getBaseContext(), MakeDrinkPage.class);
		try {
			//Log.d("epic", jsonObj.getJSONObject("recipe").toString());
			intent.putExtra("JSONstr",jsonObj.getJSONObject("recipe").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startActivity(intent);
	}
	
	public void setListData(String json) {
		listview = (ListView) findViewById(R.id.listView1);
		alist = new ArrayList<CData>();
		adapter = new DataAdapter(this, alist);
		listview.setAdapter(adapter);
		
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				JSONObject obj = jsonObject.getJSONObject(key);
				Log.d("epic", obj.toString());
				adapter.add(new CData(getApplicationContext(), obj.getString("rating"),
						obj.getString("comment")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class DataAdapter extends ArrayAdapter<CData> {
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;
			if (v == null) {
				view = mInflater.inflate(R.layout.postitem, null);
			} else {
				view = v;
			}

			// 자료를 받는다.
			final CData data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				RatingBar rb  = (RatingBar) view.findViewById(R.id.ratingBar1);
				TextView tv = (TextView) view.findViewById(R.id.textView1);
				
				rb.setRating(Float.parseFloat(data.getRating()));
				tv.setText(data.getComment());
			}
			return view;
		}
	}
	
	class CData {
		private String rating;
		private String comment;
		
		public CData(Context context, String _rating, String _comment) {
			rating = _rating;
			comment = _comment;
		}

		public String getRating() {
			return rating;
		}
		
		public String getComment() {
			return comment;
		}
	}
}
