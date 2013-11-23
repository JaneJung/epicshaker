package test.abc;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DrinkInfoPage extends Activity {
	private JSONObject jsonObj = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.drinkinfopage);
		String name = "";
		String jsonStr = "";
		Log.d("epic", jsonStr);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			jsonStr = extras.getString("JSONstr");
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView nameView =(TextView)findViewById(R.id.textView3);
		nameView.setText(name);
		
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

}
