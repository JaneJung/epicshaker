package test.abc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

public class GetArduinoData extends Activity{
	private TextView textView;
	private static UsbSerialDriver sDriver = null;
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private Method receivedDataMethod; 
    private Object activityObject;
    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
        	
          //  Log.d(TAG, "Runner stopped.");
        }

        @Override
        public void onNewData(final byte[] data) {
        	GetArduinoData.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	GetArduinoData.this.updateReceivedData(data);
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
    	try {
			receivedDataMethod.invoke(activityObject,parseInt);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    
    GetArduinoData(Object object,Method objMethod) {
    	activityObject = object;
    	receivedDataMethod = objMethod;
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		
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
	
	protected void end(){
		stopIoManager();
		try {
			sDriver.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
