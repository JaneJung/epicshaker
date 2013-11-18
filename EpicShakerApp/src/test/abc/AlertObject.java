package test.abc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertObject {
	Method method;
	Object methodThis;
	AlertObject(Method inMethod,Object inObject) {
		method = inMethod;
		methodThis = inObject;
	}
	
	public void showAlert(String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder((Context) methodThis);
		alert.setPositiveButton("»Æ¿Œ", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	dialog.dismiss();     //¥›±‚
		    	try {
					method.invoke(methodThis);
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
		});
		alert.setMessage(msg);
		alert.show();
	}
}
