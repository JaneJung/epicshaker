package test.abc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;


public class PreMakePage extends Activity {
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
        String[] arr=HexDump.dumpHexString(data).split(" ");
        String a=arr[arr.length-1];
    	String message = a.substring(0,a.length()-1);
    	message = message.trim();
    	//message = "3AB";
    	int parseInt = Integer.parseInt(message, 16);
        textView.setText(Integer.toString(parseInt));
    }

    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.premakepage);
		 textView=(TextView)findViewById(R.id.textView3);
		Button btn1=(Button)findViewById(R.id.button1);

		btn1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intetn2 = new Intent(PreMakePage.this , InputDrinkInfoPage.class);
				startActivity(intetn2);
			}
		});
		
		
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
