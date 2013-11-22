package test.abc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputDrinkInfoPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputdrinkinfopage);
		
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn2 = new Intent(InputDrinkInfoPage.this , CreateDrinkPage.class);
				
				EditText nameText=(EditText)findViewById(R.id.editText1);
				EditText desText=(EditText)findViewById(R.id.editText2);
				EditText tagText=(EditText)findViewById(R.id.editText3);

				intetn2.putExtra("name",nameText.getText().toString());
				intetn2.putExtra("des",desText.getText().toString());
				intetn2.putExtra("tag",tagText.getText().toString());

				
				startActivity(intetn2);
			}
		});
	}



}
