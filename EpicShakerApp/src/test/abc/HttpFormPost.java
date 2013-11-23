package test.abc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;


public class HttpFormPost {

	String postUrl;
	HashMap<String, String> dataHash;

	public HttpFormPost(String url) {
		dataHash = new HashMap<String, String>();
		postUrl = url;
	}

	public void setHash(String name,String data) {
		dataHash.put(name, data);
	}

	private class httpPost extends AsyncTask<String, Void, Void>{
		String jsonStr = null;

		@Override
		protected Void doInBackground(String... urls) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(postUrl);

			try {
				int len = dataHash.size();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(len);
				Iterator it = dataHash.keySet().iterator();

				while(it.hasNext()) {
					String key = (String) it.next();
					nameValuePairs.add(new BasicNameValuePair(key,dataHash.get(key)));
					Log.d("epic",key + "   " + dataHash.get(key));
				}

				/*
		            nameValuePairs.add(new BasicNameValuePair("company","1"));
		            nameValuePairs.add(new BasicNameValuePair("language","he"));
		            nameValuePairs.add(new BasicNameValuePair("freelang","480"));
		            nameValuePairs.add(new BasicNameValuePair("width","400"));
				 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				httpclient.execute(httppost);
				}catch(IOException e) {
		            e.printStackTrace();
		        }
			return null;
			}

			/*
			@Override
			protected void onPostExecute() {
				//super.onPostExecute(result);     
				
			}
			*/

		}



		public void postData() {
			new httpPost().execute(postUrl);
			// Creates a new HttpClient and Post Header

		} 
	
}
