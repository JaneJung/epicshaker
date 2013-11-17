package test.abc;

import java.io.IOException;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
 
public class CreateDrinkPage extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdrinkpage);
        LinearLayout lay1 = (LinearLayout)findViewById(R.id.layout1);
               
        View waterView= new View(this);
        waterView.setBackgroundColor(Color.YELLOW);
        waterView.setX(227);
       // waterView.setRight(120);
        waterView.setY(113);
        waterView.setAlpha(0.8f);
        waterView.setLayoutParams(new LayoutParams(200, 400));
        
        lay1.addView(waterView);
        
        waterView.setPivotY(400);
        ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(waterView, View.SCALE_Y,
                1f,0);
	    imageAnimator.setDuration(3000);
	    imageAnimator.start();
	    
	    //setContentView(waterView);
        
    
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