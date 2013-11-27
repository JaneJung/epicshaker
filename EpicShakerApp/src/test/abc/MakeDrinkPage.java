package test.abc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.R.string;
import android.os.Bundle;
import android.os.Vibrator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MakeDrinkPage extends Activity {
	private JSONObject jsonObj = null;
	int cupX, cupY, width;
	final int cupHeight = 300;
	int curWaterLevel=0;
	int colorArr[];
	int curWeight = 0;
	int waterColor=0;
	int global_cnt=0;
	int final_print=0;
	int cupCapacity = 0;
	TextView matView;
	DrawView drawView;
	private static UsbSerialDriver sDriver = null;
	View curView;
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
	String curMetarial;
	Iterator<String> curKey;
	Map<String, Integer> recipeInfo;
	String jsonStr;
	String jsonId;
	int maxValue=0;
	TextView temp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makedrinkpage);
		temp=(TextView)findViewById(R.id.textView3);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			jsonId = extras.getString("id");
			jsonStr = extras.getString("JSONstr");
			try {
				jsonObj = new JSONObject(jsonStr).getJSONObject("recipe");
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Button btn2=(Button)findViewById(R.id.button1);
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Method complete;
				Class[] parameterTypes = new Class[0];
		        try {
					complete = MakeDrinkPage.class.getMethod("complete", parameterTypes);
					AlertObject altobj = new AlertObject(complete,MakeDrinkPage.this);
					altobj.showAlert("제작이 완료되었습니다.");
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
			}
		});
	
		
		matView = (TextView)findViewById(R.id.textView2);
		curKey = jsonObj.keys();
		curWeight = ((GlobalClass) this.getApplication()).getCupWeight();
		
		setUsbSetting();
		setColor();
		setMeterial();
		createWaterView();
	}
	
	private void setUsbSetting() {
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		//Log.d("epic","123141251251");
		
		// Find the first available driver.
		sDriver = UsbSerialProber.acquire(manager);
	
		if (sDriver != null) {
			try {
				sDriver.open();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				sDriver.setBaudRate(115200);
				onDeviceStateChange();
			} catch (IOException e) {
				// Deal with error.
			} 
		} else {
			Button sb=(Button)findViewById(R.id.button3);
			sb.setOnTouchListener(new OnTouchListener() {
		       
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg0.isPressed()) {
						curWaterLevel = curWaterLevel+10;
						calculateWaterLevel(curWaterLevel);
						Log.d("epic","button data: " + Integer.toString(curWaterLevel));
					}
					return false;
				}
		    });
		}
	}
	
	
	private void setColor() {
		colorArr = new int[4];
		colorArr[0] = Color.RED;
		colorArr[1] = Color.GREEN;
		colorArr[2] = Color.YELLOW;
		colorArr[3] = Color.MAGENTA;
	}
	
	protected void setMeterial() {
		recipeInfo = new HashMap<String, Integer>();
		Iterator<String> keys;
		keys = jsonObj.keys();
		int firstFlag=0;
		while(keys.hasNext()) {
			try {
				String key = keys.next();
				maxValue = maxValue + Integer.parseInt(jsonObj.getString(key));
				if(firstFlag==0) {
					curMetarial = curKey.next();
					firstFlag=1;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		keys = jsonObj.keys();
		int yValue = 0;
		
		while(keys.hasNext()) {
			try {
				String key = keys.next();
				yValue = yValue + calBorderY(Integer.parseInt(jsonObj.getString(key)));
				recipeInfo.put(key,yValue);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		matView.setText(curMetarial);
		drawBorder();
		
	}
	
	protected int calBorderY(int y) {
		Log.d("epic","oriY: " + Integer.toString(y));
		int r;
		r = ((cupHeight) * y) / maxValue;
		
		return r;
	}
	
	protected void drawBorder() {
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout1);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		cupX = getPixels(90); 
		cupY = getPixels(cupHeight);
		drawView = new DrawView(this);
		layout.addView(drawView);
		
	}

	class DrawView extends View {
		Paint paint = new Paint();

		public DrawView(Context context) {
			super(context);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(10);
		}
		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//canvas.drawLine(cupX, 0, width-cupX, 0, paint);
			Iterator<String> key = jsonObj.keys();
			int yValue = 0;
			
			while(key.hasNext()) {
				yValue = recipeInfo.get(key.next());
				Log.d("epic","line data: " + Integer.toString(yValue));
				canvas.drawLine(cupX, getPixels(300-yValue), width-cupX, getPixels(300-yValue), paint);
			}
		}
	}
	
	public void complete() {
		//Intent intetn1 = new Intent(MakeDrinkPage.this , DrinkEvaluatePage.class);
		//
		//startActivity(intetn1);
		Intent intent = new Intent(getBaseContext(), DrinkEvaluatePage.class);
		//Log.d("epic", jsonObj.getJSONObject("recipe").toString());
		saveData();
		JSONObject jso;
		try {
			jso = new JSONObject(jsonStr);
			Log.d("epic","sdgsg      " + jso.getString("name"));
			intent.putExtra("drinkName",jso.getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intent.putExtra("id",jsonId);
		startActivity(intent);
	}
	
	private void saveData() {
		FileOutputStream outputStream;
		try {
			String saveString ="";
			saveString = "\""+ jsonId + "\":" + jsonStr + ",";
			outputStream = openFileOutput("MakeFileHistory", Context.MODE_APPEND);
			Log.d("epic",saveString);
			outputStream.write(saveString.getBytes());
		    outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
	
	private void checkWaterLevel() {
		Log.d("epic","check: " + recipeInfo.get(curMetarial));
		Log.d("epic","curWaterLevel: " + Integer.toString(curWaterLevel));

		if(curWaterLevel >= recipeInfo.get(curMetarial)) {
			Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);   
			vibe.vibrate(1000);     
			if(curKey.hasNext()) {
				curMetarial = curKey.next();
				matView.setText(curMetarial);
				createWaterView(); 
			} else {
				matView.setText("완료되었습니다.");
				TextView textxt = (TextView)findViewById(R.id.TextView01);
				textxt.setText("");
				
				//complete();
			}
		}
	}
	
	
	public void calculateWaterLevel (int waterLevel) {
		curWaterLevel = (int) (waterLevel);
		checkWaterLevel();
		//temp.setText(Integer.toString(waterLevel));
		float level = curWaterLevel/(float)cupHeight;
		waterLevelChange(level);
		//updateWaterLevel(level);
	}
	
	private int getPixels(int dipValue){ 
	     Resources r = getResources();
	     int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,   r.getDisplayMetrics());
	     return px; 
	}

	public void createWaterView () {
		RelativeLayout lay1 = (RelativeLayout)findViewById(R.id.layout1);
		//temp.setT
		View waterView= new View(this);
		
		int width = 720 - getPixels(184);
		//Log.d("width", Integer.toString(width));
		int height = getPixels(cupHeight);
		waterView.setX((float)getPixels(92));
		waterView.setBackgroundColor(colorArr[waterColor]);
		waterView.setAlpha(0.4f);
		waterView.setLayoutParams(new LayoutParams(width, height-4));

		lay1.addView(waterView);
		
		//waterView.setPivotY(400);
		waterView.setPivotY(height-4);
		curView = waterView;
		waterLevelChange(0);
		waterColor++;
	} 
	
	public void waterLevelChange(float level) {
		ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(curView, View.SCALE_Y,
				0,level);
		imageAnimator.setDuration(0);
		imageAnimator.start();
	}
	

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
        	
          //  Log.d(TAG, "Runner stopped.");
        }

        @Override
        public void onNewData(final byte[] data) {
        	MakeDrinkPage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	MakeDrinkPage.this.updateReceivedData(data);
                }
            });
        }
    };
    
    private void updateReceivedData(byte[] data) {
    	if(sDriver!=null){
    		try{
    			 //textView.setText(HexDump.dumpHexString(data));
    			
    	    	String[] arr=HexDump.dumpHexString(data).split(" ");
    	        
    	    	String a=arr[arr.length-1];
    	    	String message = a.substring(0,a.length()-1);
    	    	 //temp.setText(message);
    	    	
    	    	message = message.trim();
    	    	//message = "3AB";
    	    	int parseInt = Integer.parseInt(message, 16);
    	        
    	    	nomalize(parseInt);
     			if(global_cnt==10){
     				calculateWaterLevel(Math.abs((curWeight-final_print)/5*4));
     				global_cnt=0;
     			}
     			temp.setText(Integer.toString(Math.abs((curWeight-final_print)/10)));
     			//temp.setText(Integer.toString(Math.abs(curWeight-final_print)*2/5));
    	    	
    	    	
    	       // calculateWaterLevel(parseInt);
    	        //((GlobalClass) this.getApplication()).setCupWeight(parseInt);
			} catch (NumberFormatException e) {
				// Deal with error.
			} 
    		
    	} 
    }
    
    private int nomalize(int input){
    	if(input<10||input<(final_print/10)||Math.abs(final_print-input)<2){
    		return 0;
    	}
    	else{
      		final_print=(final_print+input)/2;
    		global_cnt++;
    		return 0;
    	}
    }
    
    
    private void stopIoManager() {
		if (mSerialIoManager != null) {
			mSerialIoManager.stop();
			mSerialIoManager = null;
		}
	}

	private void startIoManager() {
		if (sDriver != null) {
			mSerialIoManager = new SerialInputOutputManager(sDriver, mListener);
			mExecutor.submit(mSerialIoManager);
			
		}
	}

	private void onDeviceStateChange() {
		stopIoManager();
		startIoManager();
	}

}
