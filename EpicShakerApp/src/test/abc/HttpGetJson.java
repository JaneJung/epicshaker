package test.abc;

import java.io.IOException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.AsyncTask;
import android.util.Log;


public class HttpGetJson {
	
	public Method method;
	public Object methodThis;
	public String url;
	HttpGetJson(Method listMethod,Object object,String inpurl ) {
		method = listMethod;
		methodThis = object;
		url = inpurl;
		new httpRequestGet().execute(url);
	}
	
	private class httpRequestGet extends AsyncTask<String, Void, String>{
		String jsonStr = null;
	      @Override
	      protected String doInBackground(String... urls) {
	           
	          HttpResponse response = null;
	          HttpClient client = new DefaultHttpClient();
	          HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	          HttpGet httpGet = new HttpGet(urls[0]);
	          try {
	              response = client.execute(httpGet);
	              HttpEntity resEntity = response.getEntity();

	              jsonStr = EntityUtils.toString(resEntity);

	          }
	          catch(IOException e) {
	              e.printStackTrace();
	          }
	          return jsonStr;
	      }
	 
	    
	        @Override
	      protected void onPostExecute(String result) {
	          //super.onPostExecute(result);     
	          try {
	        	  Log.d("epic",result);
	        	  method.invoke(methodThis, result);
	        	 // method.invoke(method, "");
	          } catch (IllegalStateException e) {
	              e.printStackTrace();
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
	      
	  }
}
