package test.abc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Context;
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
				Intent intetn3 = new Intent(MainActivity.this , TakeDrinkPicture.class);
				startActivity(intetn3);
			}
		});
		
		try {
			makeFile("MakeHistoryData");
			makeFile("CreateHistoryData");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void makeFile(String name) throws IOException {
		
		File file = new File(getFilesDir(), name);
		if (file.exists()) {
		} else {
			FileOutputStream fos = openFileOutput(name, Context.MODE_PRIVATE);
			fos.close();
		}
	}



}
