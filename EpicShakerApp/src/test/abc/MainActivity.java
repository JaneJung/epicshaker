package test.abc;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn1 = new Intent(MainActivity.this , CreateDrinkListPage.class);
				startActivity(intetn1);
			}
		});

		
		Button btn2=(Button)findViewById(R.id.button2);
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn2 = new Intent(MainActivity.this , DrinkSearchPage.class);
				startActivity(intetn2);
			}
		});

		
		Button btn3=(Button)findViewById(R.id.button3);
		btn3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn3 = new Intent(MainActivity.this , MakeDrinkPage.class);
				startActivity(intetn3);
			}
		});

		
		
		
		
		
		/*
		Button btnOrange=(Button)findViewById(R.id.button2);
		btnOrange.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				TextView textFruit=(TextView)findViewById(R.id.fruit);
				textFruit.setText("Orange");
			}
		});
	*/	
	}

	//@Override
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/


}
