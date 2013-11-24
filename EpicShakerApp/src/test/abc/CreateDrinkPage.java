package test.abc;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import java.lang.Object;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;

import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CreateDrinkPage extends Activity {
	
	/** Called when the activity is first created. */
	String drinkName;
	String des;
	String tag;
	int waterColor=0;
	int curWaterLevel=0;
	int cupHeight=300;
	int colorArr[];
	int curWeight = 0;
	
	/* nomalize variables by choragi*/
	int global_cnt=0;
	int final_print=0;
	
	HttpFormPost formPost;
	View curView;
	static int MetrailActivityCode=0;
	JSONObject meterialObj;
	private static UsbSerialDriver sDriver = null;
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
	TextView temp;
	Button sb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createdrinkpage);
		//cupHeight = ((GlobalClass) this.getApplication()).getCupWeight();
		setColor();

		Class[] parameterTypes = new Class[1];
        parameterTypes[0] = Integer.class;
		
		meterialObj = new JSONObject();
		formPost = new HttpFormPost("http://epicshakerprj.appspot.com/addrecipe/");
		
		//임시 코드
		//textView2
		temp=(TextView)findViewById(R.id.textView2);
		
		curWeight = ((GlobalClass) this.getApplication()).getCupWeight();
		temp.setText(Integer.toString(curWeight));
		
		

		createWaterView();


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    drinkName = extras.getString("name");
			Log.d("epic",drinkName);
		    des = extras.getString("des");
		    tag = extras.getString("tag");
		    formPost.setHash("name", drinkName);
		    formPost.setHash("description", des);
		    formPost.setHash("tags", tag);
		    
		}
		
		
		
		Button btn1=(Button)findViewById(R.id.button1);
		
		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn1 = new Intent(CreateDrinkPage.this , MaterialListPage.class);
				//intetn1.putExtra("Name", Value);
				startActivityForResult(intetn1,MetrailActivityCode);
				//startActivity(intetn1);
			}
		});
		Button btn2=(Button)findViewById(R.id.button2);
		btn2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Method complete;
				Class[] parameterTypes = new Class[0];
		        try {
		        	formPost.setHash("recipe",meterialObj.toString());
		        	formPost.postData();
					complete = CreateDrinkPage.class.getMethod("complete", parameterTypes);
					AlertObject altobj = new AlertObject(complete,CreateDrinkPage.this);
					altobj.showAlert("제작이 완료되었습니다.");
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
			}
		});

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
			sb=(Button)findViewById(R.id.button3);
			sb.setOnTouchListener(new OnTouchListener() {
		       
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg0.isPressed()) {
						curWaterLevel = curWaterLevel+10;
						calculateWaterLevel(curWaterLevel);
						Log.d("epic",Integer.toString(curWaterLevel));
					}
					return false;
				}
		    });
		}
		
		
	}
	
	private void setColor() {
		colorArr = new int[4];
		colorArr[0] = Color.YELLOW;
		colorArr[1] = Color.GREEN;
		colorArr[2] = Color.RED;
		colorArr[3] = Color.MAGENTA;
	}
	
	
	@Override
	public void onStop() {
	    super.onStop();  // Always call the superclass method first
	    //middleTest++;
	   
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == MetrailActivityCode && resultCode == Activity.RESULT_OK) {
	        String metrialName = data.getStringExtra("name");
	        try {
				meterialObj.put(metrialName,curWaterLevel);
				TextView metarailText=(TextView)findViewById(R.id.TextView01);
				metarailText.setText(metrialName);
				createWaterView();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //Log.d("epic",metrialName);
	        // do something with B's return values
	    }
	}
	
	
	@Override
	public void onRestart() {
	    super.onRestart();  // Always call the superclass method first
	  
	}
	
	public void complete() {
		Intent intetn1 = new Intent(CreateDrinkPage.this , MainActivity.class);
		startActivity(intetn1);
	}
	
	
	public void calculateWaterLevel (int waterLevel) {
		curWaterLevel = (int) (waterLevel);
		temp.setText(Integer.toString(waterLevel));
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
        	CreateDrinkPage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	CreateDrinkPage.this.updateReceivedData(data);
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
    	      
    	        
    	        /* nomalization code by choragi */
    	        nomalize(parseInt);
    			if(global_cnt==10){
    				calculateWaterLevel(final_print/10);
    				global_cnt=0;
    			}
    			temp.setText(Integer.toString(final_print/10));
    	        //((GlobalClass) this.getApplication()).setCupWeight(parseInt);
			} catch (NumberFormatException e) {
				// Deal with error.
			} 
    		
    	} 
    }
    
    /* nomalization function by choragi */    
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

