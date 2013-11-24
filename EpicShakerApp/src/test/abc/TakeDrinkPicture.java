package test.abc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class TakeDrinkPicture extends Activity implements OnClickListener {

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private Uri mImageCaptureUri;
	private ImageView mPhotoImageView;
	private Button mButton;
	
	private File file;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takedrinkpicture);

		mButton = (Button) findViewById(R.id.button);
		mPhotoImageView = (ImageView) findViewById(R.id.image);

		mButton.setOnClickListener(this);
	}

	/**
	 * ī�޶󿡼� �̹��� ��������
	 */
	private void doTakePhotoAction()
	{
		/*
		 * ���� �غ���
		 * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
		 * http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// �ӽ÷� ����� ������ ��θ� ����
		String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		mImageCaptureUri = Uri.fromFile(
				new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), url));
	
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		// Ư����⿡�� ������ ������ϴ� ������ �־� ������ �ּ�ó�� �մϴ�.
		//intent.putExtra("return-data", true);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	/**
	 * �ٹ����� �̹��� ��������
	 */
	private void doTakeAlbumAction()
	{
		// �ٹ� ȣ��
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_OK)
		{
			return;
		}

		switch(requestCode)
		{
		case CROP_FROM_CAMERA:
		{
			// ũ���� �� ������ �̹����� �Ѱ� �޽��ϴ�.
			// �̹����信 �̹����� �����شٰų� �ΰ����� �۾� ���Ŀ�
			// �ӽ� ������ �����մϴ�.
			final Bundle extras = data.getExtras();

			if(extras != null)
			{
				Bitmap photo = extras.getParcelable("data");
				mPhotoImageView.setImageBitmap(photo);
			}

			Log.d("epic", "1!!");
			// �ӽ� ���� ����
			file = new File(mImageCaptureUri.getPath());
			Log.d("epic", "path : " + mImageCaptureUri.getPath());
			Log.d("epic", "file : " + file.toString());
			if(file.exists())
			{
				Log.d("epic", "3!!");
				uploadImage(file);
				//file.delete();
			}

			break;
		}

		case PICK_FROM_ALBUM:
		{
			// ������ ó���� ī�޶�� �����Ƿ� �ϴ�  break���� �����մϴ�.
			// ���� �ڵ忡���� ���� �ո����� ����� �����Ͻñ� �ٶ��ϴ�.

			mImageCaptureUri = data.getData();
		}

		case PICK_FROM_CAMERA:
		{
			// �̹����� ������ ������ ���������� �̹��� ũ�⸦ �����մϴ�.
			// ���Ŀ� �̹��� ũ�� ���ø����̼��� ȣ���ϰ� �˴ϴ�.
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");

			intent.putExtra("outputX", 90);
			intent.putExtra("outputY", 90);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, CROP_FROM_CAMERA);

			break;
		}
		}
	}

	@Override
	public void onClick(View v)
	{
		DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				doTakePhotoAction();
			}
		};

		DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				doTakeAlbumAction();
			}
		};

		DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		};

		new AlertDialog.Builder(this)
		.setTitle("���ε��� �̹��� ����")
		.setPositiveButton("�����Կ�", cameraListener)
		.setNeutralButton("�ٹ�����", albumListener)
		.setNegativeButton("���", cancelListener)
		.show();
	}

	private void uploadImage(File imageFile) {
		Log.d("epic", "upload!!");
		new HttpClient().execute(mImageCaptureUri);
		
	}
	
	public class HttpClient extends AsyncTask<Uri, Void, Void> {

		@Override
		protected Void doInBackground(Uri... params) {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://epicshakerprj.appspot.com/uploadimage/");

			try {
				MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				mpEntity.addPart("id", new StringBody("4817033455730688"));
				mpEntity.addPart("img", new FileBody(file, "image/jpeg"));
				httppost.setEntity(mpEntity);
				HttpResponse response;
				Log.d("epic", "before execute!!");
				response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String responseText = EntityUtils.toString(entity);
				//file.delete();
				Log.d("epic", response.getStatusLine().toString());
				Log.d("epic", responseText);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Long unused) {
			/*
			progressDialog.dismiss();

			((Runnable) ctx ).run();

			super.onPostExecute(unused);
			*/
		}

	}
}
