package test.abc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class InputDrinkInfoPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputdrinkinfopage);
		
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn2 = new Intent(InputDrinkInfoPage.this , CreateDrinkPage.class);
				startActivity(intetn2);
			}
		});
	}



}
