package test.abc;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MakeDrinkPage extends Activity {

	int cupX, cupY, width;
	DrawView drawView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makedrinkpage);

		test();

	}
	
	protected void test() {
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout1);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		cupX = getPixels(90); 
		cupY = getPixels(300);
		
		drawView = new DrawView(this);
		layout.addView(drawView);
		
	}
	
	private int getPixels(int dipValue){ 
	     Resources r = getResources();
	     int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
	     return px; 
	}

	class DrawView extends View {
		Paint paint = new Paint();

		public DrawView(Context context) {
			super(context);
			paint.setColor(Color.BLUE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(20);
		}
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawLine(cupX, 0, width-cupX, 0, paint);
			canvas.drawLine(cupX, cupY, width-cupX, cupY, paint);
		}
	}

	protected void calculateRate(JSONObject jsonObject) {
		Iterator<?> keys = jsonObject.keys();
		int totalAmount = 0;
		while (keys.hasNext()) {
			String key = (String) keys.next();
			try {
				totalAmount += jsonObject.getInt(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		keys = jsonObject.keys();
		//while (keys.hasNext()) {
		String key = (String) keys.next();
		Paint paint = new Paint();
		paint.setStrokeWidth(6f);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		Canvas canvas = new Canvas();
		canvas.drawLine(50, 50, 200, 50, paint);
		//}


	}

}
