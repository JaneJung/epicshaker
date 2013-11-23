package test.abc;


import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;


public class DrinkNameSearchPage extends Activity   {

	// ����Ʈ�� ����
	private ListView listview;
	public SearchView searchView;

	// �����͸� ������ Adapter
	DataAdapter  adapter;

	// �����͸� ���� �ڷᱸ��
	ArrayList<CData> alist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinknamesearchpage);
		
		listview = (ListView) findViewById(R.id.listView1);
		OnItemClickListener listViewClickListener = new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id)
			{
//				clickedView.get
				sendDataToDrinkInfo(position);
				//MaterialListPage.this.finish(); 
			}


		};
		listview.setOnItemClickListener(listViewClickListener);
		
		try {
			Method setListMethod;
			Class[] parameterTypes = new Class[1];
	        parameterTypes[0] = String.class;
			setListMethod = DrinkNameSearchPage.class.getMethod("setListData", parameterTypes);
			new HttpGetJson(setListMethod,DrinkNameSearchPage.this,"http://epicshakerprj.appspot.com/recipelist/");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		searchView = (SearchView) findViewById(R.id.editText1);
		OnQueryTextListener searchViewQueryListner = new SearchView.OnQueryTextListener( ) {
			@Override
			public boolean onQueryTextChange( String newText ) {
				Method setListMethod;
				try {
					Class[] parameterTypes = new Class[1];
					parameterTypes[0] = String.class;
					setListMethod = DrinkNameSearchPage.class.getMethod("setListData", parameterTypes);
					if (!newText.isEmpty())
						new HttpGetJson(setListMethod,DrinkNameSearchPage.this,"http://epicshakerprj.appspot.com/recipe/?name="+newText);
					else
						new HttpGetJson(setListMethod,DrinkNameSearchPage.this,"http://epicshakerprj.appspot.com/recipelist/");
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
	
	public void sendDataToDrinkInfo(int position) {		
		Intent intent = new Intent(getBaseContext(), DrinkInfoPage.class);
		intent.putExtra("JSONstr",adapter.getItem(position).getJSON().toString());
		startActivity(intent);
	}
	
	public void setListData(String json) {
		//theJsonString = new String(" {\"5668600916475904\": {\"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/02/85/28581.jpg\",       \"tags\": [         \"cool\"       ],       \"recipe\": {         \"vodka\": 21,         \"pineapple juice\": 28,         \"coconut rum\": 28       },       \"name\": \"Bikini Martini\",       \"description\": \"An awesome drink to sip by the pool! Garnish with an orange wheel.\"     },     \"5649050225344512\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/03/46/34689.jpg\",       \"tags\": [         \"sweet\"       ],       \"recipe\": {         \"gin\": 42,         \"vodca\": 56       },       \"name\": \"Dan Fay Martini\",       \"description\": \"My father believes in a condition called 'the gin meanies.' He invented this cocktail to satisfy his taste for gin, while allowing him to slightly dilute its effects with vodka. His passion for this concoction quickly spread to my friends, who have been known to throw parties solely to celebrate him and his martini.\"     },     \"5750085036015616\": {       \"image\": null,       \"tags\": [],       \"recipe\": {         \"tequila\": 170,         \"triple sec\": 56,         \"frozen sliced strawberries\": 226       },       \"name\": \"epicshaker\",       \"description\": null     },     \"5178081291534336\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/23/47/234705.jpg\",       \"tags\": [         \"sweet\",         \"cool\"       ],       \"recipe\": {         \"tequila\": 170,         \"triple sec\": 56,         \"frozen sliced strawberries\": 226       },       \"name\": \"Ultimate Frozen Strawberry Margarita\",       \"description\": \"A near perfect strawberry margarita with frozen strawberries and limeade concentrate.\"     },     \"5741031244955648\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/39/74/397413.jpg\",       \"tags\": [         \"sweet\"       ],       \"recipe\": {         \"tequila\": 141,         \"fresh lime juice\": 85,         \"triple sec\": 85,         \"sweetened lime juice\": 28       },       \"name\": \"Parker's Famous Margaritas\",       \"description\": \"This is the signature drink at my in-law's home. My father-in-law developed a taste for margaritas made from scratch during the summer they spent in Zihuatanejo during the 1960s. After decades of tinkering he has arrived at this foolproof recipe for the ultimate Mexican cocktail.\"     },     \"5724160613416960\": {       \"image\": \"http://images.media-allrecipes.com/userphotos/250x250/00/64/75/647594.jpg\",       \"tags\": [         \"sweet\",         \"cool\"       ],       \"recipe\": {         \"coconut milk\": 28,         \"pineapple juice\": 28,         \"rum\": 14       },       \"name\": \"Pina Colada III\",       \"description\": \"A simple run-of-the-mill pina colada drink.\"     }   }");
		//Log.d("epic", "dsgswdgsdgds");
		
		String theJsonString = json;
		
		// ������ ����Ʈ�信 ����� �����俬��
		listview = (ListView) findViewById(R.id.listView1);

		// ��ü�� �����մϴ�
		alist = new ArrayList<CData>();

		// �����͸� �ޱ����� �����;���� ��ü ����
		adapter = new DataAdapter(this, alist);

		// ����Ʈ�信 ����� ����
		listview.setAdapter(adapter);

		// ArrayAdapter�� ���ؼ� ArrayList�� �ڷ� ����
		// �ϳ��� String���� �����ϴ� ���� CDataŬ������ ��ü�� �����ϴ������� ����
		// CData ��ü�� �����ڿ� ����Ʈ ǥ�� �ؽ�Ʈ��1�� ����� �ؽ�Ʈ��2�� ���� �׸��� �����̹������� ����Ϳ� ����

		// CDataŬ������ ���鶧�� ������� �ش� �μ����� �Է�
		// ���� ������ ����Ʈ�信 �ѷ��� ���� �����̶�� �����ϸ� �˴ϴ�.

		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
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
						map.get(key).getString("description"), 1,map.get(key)));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	public class DataAdapter extends ArrayAdapter<CData> {
		// ���̾ƿ� XML�� �о���̱� ���� ��ü
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {

			// ���� Ŭ������ �ʱ�ȭ ����
			// context, 0, �ڷᱸ��
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// �������� ��Ÿ���� �ڽ��� ���� xml�� ���̱� ���� ����
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// ���� ����Ʈ�� �ϳ��� �׸� ���� ��Ʈ�� ���

			if (v == null) {

				// XML ���̾ƿ��� ���� �о ����Ʈ�信 ����
				view = mInflater.inflate(R.layout.drinkitem, null);
			} else {

				view = v;
			}

			// �ڷḦ �޴´�.
			final CData data = this.getItem(position);

			if (data != null) {
				// ȭ�� ���
				TextView tv = (TextView) view.findViewById(R.id.textView1);
				TextView tv2 = (TextView) view.findViewById(R.id.textView2);
				// �ؽ�Ʈ��1�� getLabel()�� ��� �� ù��° �μ���
				tv.setText(data.getLabel());
				// �ؽ�Ʈ��2�� getData()�� ��� �� �ι�° �μ���
				tv2.setText(data.getData());
				//tv2.setTextColor(Color.WHITE);
				ImageView iv = (ImageView) view.findViewById(R.id.imageView1);

				/*
						new Thread() {
			                public void run() {
			                    try {
			                        String url = "http://img823.imageshack.us/img823/5467/bb9000.png";
			                        InputStream is = (InputStream) new URL(url).getContent();
			                        Bitmap bmp = BitmapFactory.decodeStream(is);
			                        iv.setImageBitmap(bmp);
			                       //Bitmap�� ImageView�� ����
			                    } catch (Exception e) {
			                    }
			                }
			            }.start();
				 */


				// �̹����信 �ѷ��� �ش� �̹������� ���� �� ����° �μ���
				//iv.setImageResource(data.getData2());

			}

			return view;

		}

	}


	class CData {

		private String m_szLabel;
		private String m_szData;
		private int m_szData2;
		private JSONObject jsonObj;

		public CData(Context context, String p_szLabel, String p_szDataFile,
				int p_szData2, JSONObject json) {

			m_szLabel = p_szLabel;

			m_szData = p_szDataFile;

			m_szData2 = p_szData2;
			

			jsonObj = json;
		}

		public JSONObject getJSON() {
			return jsonObj;
		}
		
		public String getLabel() {
			return m_szLabel;
		}

		public String getData() {
			return m_szData;
		}

		public int getData2() {
			return m_szData2;
		}
	}
}
