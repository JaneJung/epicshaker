package test.abc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrinkSearchPage extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinksearchpage);
		
		
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn1 = new Intent(DrinkSearchPage.this , DrinkNameSearchPage.class);
				startActivity(intetn1);
			}
		});

		
		Button btn2=(Button)findViewById(R.id.button2);
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn2 = new Intent(DrinkSearchPage.this , DrinkAdvancedSearchPage.class);
				startActivity(intetn2);
			}
		});
	}
}
