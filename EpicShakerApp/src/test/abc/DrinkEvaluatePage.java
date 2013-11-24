package test.abc;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class DrinkEvaluatePage extends Activity {
	HttpFormPost formPost;
	String DrinkId="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinkevaluatepage);
		
		Bundle extras = getIntent().getExtras();
		String DrinkName="";
		
		if (extras != null) {
			DrinkId = extras.getString("id");
			DrinkName= extras.getString("drinkName");
		}
		Log.d("epic","id: " + DrinkId);
		Log.d("epic","name: " + DrinkName);
		
		
		TextView nameText=(TextView)findViewById(R.id.textView3);
		nameText.setText(DrinkName);
		
		formPost = new HttpFormPost("http://epicshakerprj.appspot.com/addpost/");
		
		
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				JSONObject jsobj = new JSONObject();
				
				String text;
				EditText edit=(EditText)findViewById(R.id.editText1);
				text =  edit.getText().toString();
				
				RatingBar ratbar=(RatingBar)findViewById(R.id.ratingBar1);
				Float barVal = ratbar.getRating();
				
				formPost.setHash("beverage_id", DrinkId);
				formPost.setHash("rating", barVal.toString());
				formPost.setHash("comment", text);
				formPost.postData();
				Intent intetn2 = new Intent(DrinkEvaluatePage.this , MainActivity.class);
				
				
				
				
				startActivity(intetn2);
			}
		}); 
	}
	
	private void makeJson() {
		
		
	}

}
