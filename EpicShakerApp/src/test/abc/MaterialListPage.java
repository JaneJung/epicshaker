package test.abc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import test.abc.DrinkNameSearchPage.CData;
import test.abc.DrinkNameSearchPage.DataAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.SearchView;

public class MaterialListPage extends Activity {

	public ListView listview;
	public SearchView searchView;
	// 데이터를 연결할 Adapter
	DataAdapter  adapter;
	Map<String, JSONObject> map;
	// 데이터를 담을 자료구조
	ArrayList<CData> alist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.materiallistpage);
		listview = (ListView) findViewById(R.id.listView1);
		OnItemClickListener listViewClickListener = new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id)
			{
//				clickedView.get
				sendDataToCreateDrink(position);
				//MaterialListPage.this.finish(); 
			}


		};
		listview.setOnItemClickListener(listViewClickListener);



		try {
			Method setListMethod;
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = String.class;
			setListMethod = MaterialListPage.class.getMethod("setListData", parameterTypes);
			new HttpGetJson(setListMethod,MaterialListPage.this,"http://epicshakerprj.appspot.com/ingredientlist/");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		setListMethod.
		searchView = (SearchView) findViewById(R.id.editText1);
		OnQueryTextListener searchViewQueryListner = new SearchView.OnQueryTextListener( ) {
			@Override
			public boolean onQueryTextChange( String newText ) {
				Method setListMethod;
				try {
					Class[] parameterTypes = new Class[1];
					parameterTypes[0] = String.class;
					setListMethod = MaterialListPage.class.getMethod("setListData", parameterTypes);
					if (!newText.isEmpty())
						new HttpGetJson(setListMethod,MaterialListPage.this,"http://epicshakerprj.appspot.com/ingredient/?name="+newText);
					else
						new HttpGetJson(setListMethod,MaterialListPage.this,"http://epicshakerprj.appspot.com/ingredientlist/");
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return false;
			}

			@Override
			public boolean   onQueryTextSubmit(String query) {

				//searchView.setText(query);
				return false;
			}
		};
		searchView.setOnQueryTextListener(searchViewQueryListner);

	}


	public void sendDataToCreateDrink(int position) {
		Intent resultIntent = new Intent();
		//resultIntent.put
		Log.d("epic","ps: " + Integer.toString(position));
		Log.d("epic","mp: " + adapter.getItem(position).getLabel());
		resultIntent.putExtra("name",adapter.getItem(position).getLabel());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();

	}

	public void setListData(String json) {
		//theJsonString = new String(" {\"5668600916475904\": {\"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/02/85/28581.jpg\",       \"tags\": [         \"cool\"       ],       \"recipe\": {         \"vodka\": 21,         \"pineapple juice\": 28,         \"coconut rum\": 28       },       \"name\": \"Bikini Martini\",       \"description\": \"An awesome drink to sip by the pool! Garnish with an orange wheel.\"     },     \"5649050225344512\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/03/46/34689.jpg\",       \"tags\": [         \"sweet\"       ],       \"recipe\": {         \"gin\": 42,         \"vodca\": 56       },       \"name\": \"Dan Fay Martini\",       \"description\": \"My father believes in a condition called 'the gin meanies.' He invented this cocktail to satisfy his taste for gin, while allowing him to slightly dilute its effects with vodka. His passion for this concoction quickly spread to my friends, who have been known to throw parties solely to celebrate him and his martini.\"     },     \"5750085036015616\": {       \"image\": null,       \"tags\": [],       \"recipe\": {         \"tequila\": 170,         \"triple sec\": 56,         \"frozen sliced strawberries\": 226       },       \"name\": \"epicshaker\",       \"description\": null     },     \"5178081291534336\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/23/47/234705.jpg\",       \"tags\": [         \"sweet\",         \"cool\"       ],       \"recipe\": {         \"tequila\": 170,         \"triple sec\": 56,         \"frozen sliced strawberries\": 226       },       \"name\": \"Ultimate Frozen Strawberry Margarita\",       \"description\": \"A near perfect strawberry margarita with frozen strawberries and limeade concentrate.\"     },     \"5741031244955648\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/39/74/397413.jpg\",       \"tags\": [         \"sweet\"       ],       \"recipe\": {         \"tequila\": 141,         \"fresh lime juice\": 85,         \"triple sec\": 85,         \"sweetened lime juice\": 28       },       \"name\": \"Parker's Famous Margaritas\",       \"description\": \"This is the signature drink at my in-law's home. My father-in-law developed a taste for margaritas made from scratch during the summer they spent in Zihuatanejo during the 1960s. After decades of tinkering he has arrived at this foolproof recipe for the ultimate Mexican cocktail.\"     },     \"5724160613416960\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/64/75/647594.jpg\",       \"tags\": [         \"sweet\",         \"cool\"       ],       \"recipe\": {         \"coconut milk\": 28,         \"pineapple juice\": 28,         \"rum\": 14       },       \"name\": \"Pina Colada III\",       \"description\": \"A simple run-of-the-mill pina colada drink.\"     }   }");

		String theJsonString = json;

		// 선언한 리스트뷰에 사용할 리스뷰연결
		listview = (ListView) findViewById(R.id.listView1);

		// 객체를 생성합니다
		alist = new ArrayList<CData>();

		// 데이터를 받기위해 데이터어댑터 객체 선언
		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);

		// ArrayAdapter를 통해서 ArrayList에 자료 저장
		// 하나의 String값을 저장하던 것을 CData클래스의 객체를 저장하던것으로 변경
		// CData 객체는 생성자에 리스트 표시 텍스트뷰1의 내용과 텍스트뷰2의 내용 그리고 사진이미지값을 어댑터에 연결

		// CData클래스를 만들때의 순서대로 해당 인수값을 입력
		// 한줄 한줄이 리스트뷰에 뿌려질 한줄 한줄이라고 생각하면 됩니다.
		map = new HashMap<String, JSONObject>();
		try {
			JSONObject jsonObject = new JSONObject(theJsonString);
			Iterator<String> keys = jsonObject.keys();

			while (keys.hasNext()) {
				String key = (String) keys.next();

				map.put(key, jsonObject.getJSONObject(key));
			}
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();

				adapter.add(new CData(getApplicationContext(), map.get(key).getString("name"),
						map.get(key).getString("image"), 1));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}



	public class DataAdapter extends ArrayAdapter<CData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;



			if (v == null) {

				view = mInflater.inflate(R.layout.drinkitem, null);
			} else {

				view = v;
			}

			// 자료를 받는다.
			final CData data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.textView1);
				TextView tv2 = (TextView) view.findViewById(R.id.textView2);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getLabel());
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				tv2.setText("");
				//tv2.setTextColor(Color.WHITE);
//				ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
//				String imageUrl = data.getImageUrl();
//				if (imageUrl != "null") {
//					UrlImageViewHelper.setUrlDrawable(iv, imageUrl);
//				}

				// 이미지뷰에 뿌려질 해당 이미지값을 연결 즉 세번째 인수값
				//iv.setImageResource(data.getData2());

			}

			return view;

		}

	}

	class CData {

		private String m_szLabel;
		private String imageUrl;
		private int m_szData2;

		public CData(Context context, String p_szLabel, String p_szDataFile,
				int p_szData2) {

			m_szLabel = p_szLabel;

			imageUrl = p_szDataFile;

			m_szData2 = p_szData2;

		}

		public String getLabel() {
			return m_szLabel;
		}

		public int getData2() {
			return m_szData2;
		}
		
		public String getImageUrl() {
			return imageUrl;
		}
	}
}
