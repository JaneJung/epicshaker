package test.abc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class CreateDrinkListPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createdrinklistpage);
		
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn2 = new Intent(CreateDrinkListPage.this , PreMakePage.class);
				startActivity(intetn2);
			}
		});

	}

	
	
}
