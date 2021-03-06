package test.abc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;


public class PreMakePage extends Activity {
	/* nomalize variables by choragi*/
	int global_cnt=0;
	int final_print=0;
	String drinkId="";
	String jsonStr="";
	private TextView textView;
	private static UsbSerialDriver sDriver = null;
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    
    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
        	
          //  Log.d(TAG, "Runner stopped.");
        }

        @Override
        public void onNewData(final byte[] data) {
        	PreMakePage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	PreMakePage.this.updateReceivedData(data);
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
    	    	 textView.setText(message);
    	    	
    	    	message = message.trim();
    	    	//message = "3AB";
    	    	int parseInt = Integer.parseInt(message, 16);
    	        nomalize(parseInt);
    			if(global_cnt==10){
    				((GlobalClass) this.getApplication()).setCupWeight(parseInt);
    				global_cnt=0;
    			}
    			textView.setText(Integer.toString(parseInt/10));
    	        
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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premakepage);
		 textView=(TextView)findViewById(R.id.textView3);
		Button btn1=(Button)findViewById(R.id.button1);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			 drinkId = extras.getString("id");
			 jsonStr = extras.getString("JSONstr");
		
			btn1.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(getBaseContext(), MakeDrinkPage.class);
					EditText edit1=(EditText)findViewById(R.id.editText1);
					intent.putExtra("cupCapacity",edit1.getText().toString() );
					//Log.d("epic", jsonObj.getJSONObject("recipe").toString());
					intent.putExtra("JSONstr",jsonStr);
					intent.putExtra("id",drinkId);
					startActivity(intent);
				}
			});
			
			
		} else {
			btn1.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Intent intetn2 = new Intent(PreMakePage.this , InputDrinkInfoPage.class);
					startActivity(intetn2);
				}
			});
		}
		
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

		
		
		// Find the first available driver.
		sDriver = UsbSerialProber.acquire(manager);
		//String a = sDriver.getDevice().getDeviceName();
		//textView.setText(a);
		
		if (sDriver != null) {
			try {
				sDriver.open();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				sDriver.setBaudRate(115200);
				// 
				//byte buffer[] = new byte[16];
				//int numBytesRead = sDriver.read(buffer, 1000);
				//Log.d("epic", "Read " + numBytesRead + " bytes.");
				 onDeviceStateChange();
			} catch (IOException e) {
				// Deal with error.
			} 
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
