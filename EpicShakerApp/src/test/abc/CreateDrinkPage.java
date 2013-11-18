package test.abc;

import java.io.IOException;
import java.lang.reflect.Method;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;

import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CreateDrinkPage extends Activity {
	/** Called when the activity is first created. */

	int waterColor;
	int curWaterLevel=0;
	final int cupHeight=300;
	int middleTest=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createdrinkpage);

		
		
		calculateWaterLevel(100);
		
		//calculateWaterLevel(250);
		Button btn1=(Button)findViewById(R.id.button1);
		
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn1 = new Intent(CreateDrinkPage.this , MaterialListPage.class);
				
				startActivity(intetn1);
			}
		});
		Button btn2=(Button)findViewById(R.id.button2);
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Method complete;
				Class[] parameterTypes = new Class[0];
		        try {
					complete = CreateDrinkPage.class.getMethod("complete", parameterTypes);
					AlertObject altobj = new AlertObject(complete,CreateDrinkPage.this);
					altobj.showAlert("제작이 완료되었습니다.");
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
			}
		});

	}
	
	
	@Override
	public void onStop() {
	    super.onStop();  // Always call the superclass method first
	    middleTest++;
	   
	}
	
	
	@Override
	public void onRestart() {
	    super.onRestart();  // Always call the superclass method first
	    if(middleTest == 1) {
	    	calculateWaterLevel(200);
	    } else if(middleTest == 2) {
	    	calculateWaterLevel(250);
	    }
	}
	
	public void complete() {
		Intent intetn1 = new Intent(CreateDrinkPage.this , MainActivity.class);
		startActivity(intetn1);
	}
	
	
	public void calculateWaterLevel (int waterLevel) {
		curWaterLevel = waterLevel;
		float level = curWaterLevel/(float)cupHeight;
		// if level > 1 => error
		if (level > 0.8f)
			waterColor = Color.YELLOW;
		else if (level > 0.5f ) {// to change water color
			waterColor = Color.RED;
		} else {
			waterColor = Color.GREEN;
		}
		updateWaterLevel(level);
	}
	
	private int getPixels(int dipValue){ 
	     Resources r = getResources();
	     int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,   r.getDisplayMetrics());
	     return px; 
	}

	public void updateWaterLevel (float level) {
		RelativeLayout lay1 = (RelativeLayout)findViewById(R.id.layout1);
		
		
		View waterView= new View(this);
		int width = 720 - getPixels(184);
		Log.d("width", Integer.toString(width));
		int height = getPixels(cupHeight);
		waterView.setX((float)getPixels(92));
		waterView.setBackgroundColor(waterColor);
		waterView.setAlpha(0.4f);
		waterView.setLayoutParams(new LayoutParams(width, height-4));

		lay1.addView(waterView);

		//waterView.setPivotY(400);
		waterView.setPivotY(height-4);
		ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(waterView, View.SCALE_Y,
				0,level);
		imageAnimator.setDuration(1000);
		imageAnimator.start();
	} 



	public void connectArduino() throws IOException{
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

		UsbSerialDriver driver = UsbSerialProber.acquire(manager);

		if(driver != null) {
			driver.open();
			try{
				driver.setBaudRate(115200);

				byte buffer[] = new byte[16];
				int numBytesRead = driver.read(buffer,1000);
				EditText text1=(EditText)findViewById(R.id.editText1);
				text1.setText(numBytesRead);
			} catch (IOException e) {
			} finally {
				driver.close();
			}
		}

	}

}


class ResizeAnimation extends Animation {
	private View mView;
	private float mToHeight;
	private float mFromHeight;

	private float mToWidth;
	private float mFromWidth;

	public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
		mToHeight = toHeight;
		mToWidth = toWidth;
		mFromHeight = fromHeight;
		mFromWidth = fromWidth;
		mView = v;
		setDuration(300);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float height =
				(mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
		float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
		LayoutParams p = mView.getLayoutParams();
		p.height = (int) height;
		p.width = (int) width;
		mView.requestLayout();
	}
}
