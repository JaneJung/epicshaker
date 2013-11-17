package test.abc;

import java.io.IOException;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;

import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
 
public class CreateDrinkPage extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdrinkpage);
      
        View waterView= new View(this);
        waterView.setLeft(0);
        waterView.setTop(40);
        waterView.setPivotY(200);
        ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(waterView, View.SCALE_Y,
                0,1f );
	    imageAnimator.setDuration(1000);
	    imageAnimator.start();
	    //setContentView(waterView);
        
        /*
        ImageView iv = (ImageView)findViewById(R.id.imageView1);
        ResizeAnimation ra = new ResizeAnimation(iv, 100, 100, 500, 500);

        Transformation t = new Transformation();
        ra.applyTransformation(1000, t);
        /*
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.test02);
        Bitmap resized = Bitmap.createScaledBitmap(image, 450, 200, true);
        iv.setImageBitmap(resized);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다
        iv.setPadding(3, 3, 3, 3);
        iv.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0) {
                finish();
            }
        });
        */
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