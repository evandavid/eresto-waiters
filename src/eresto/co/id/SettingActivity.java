package eresto.co.id;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import eresto.co.id.model.Eresto;
import eresto.co.id.model.Users;
import eresto.co.id.views.CustomEditText;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class SettingActivity extends Activity {
	private CustomEditText nameBox, ipBox, namespaceBox;
	private String name, ip, namespace, url;
	private boolean test;
	private ProgressDialog dialog;
	
	private final Handler myHandler = new Handler();
    final Runnable updateRunnable = new Runnable() {
        public void run() {
            testConnectionDOne();
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		String tmp = getIntent().getStringExtra("status");
		if (tmp != null){
			if (tmp.equals("not connected")){
				Toast.makeText(this, "Not connected to server, change server IP", Toast.LENGTH_SHORT).show();
			}
		}
		
		this.initialize();
	}
	
	private void initialize(){
		this.nameBox = (CustomEditText)findViewById(R.id.restoname);
		this.ipBox = (CustomEditText)findViewById(R.id.ip);
		this.namespaceBox = (CustomEditText)findViewById(R.id.namespace);
		Eresto app = Eresto.findById(Eresto.class, (long)1 );
		if (app != null){
			this.nameBox.setText(app.name);
			this.ipBox.setText(app.ip);
			this.namespaceBox.setText(app.namespace);
		}
	}
	
	public void doneHeader(View view){
		Log.v("orm", "clicked");
		this.name = this.nameBox.getText().toString();
		this.ip = this.ipBox.getText().toString();
		this.namespace = this.namespaceBox.getText().toString();
		
		if (this.name.equals("") || this.ip.equals("")){
			Toast.makeText(this, "Resto name or Server IP cannot be blank", Toast.LENGTH_SHORT).show();
		}else{
			dialog = ProgressDialog.show(this, "Check connection", 
					"Please wait, system checking connection with server", true);
			Eresto app = Eresto.findById(Eresto.class, (long)1 );
			if (app == null){
				app = new Eresto(this, this.name, this.ip, this.namespace);
				app.save();
			}else{
				app.name = this.name;
				app.ip = this.ip;
				app.namespace = this.namespace;
				app.save();
			}
			url = app.url()+"/api/v1/test.json";
			Log.v("orm", url);
			new Thread(new Runnable() {
    			public void run() {
    				
    				isConnectedToServer(url, 5000);
    			} 		
    		}).start();
			
		}
	}
	
	
	public void isConnectedToServer(String url, int timeout) {
		
		boolean isReachable = false;
		try { 
	    
	    	HttpGet httpPost = new HttpGet(url);

	    	HttpParams httpParameters = new BasicHttpParams();
		    int timeoutConnection = timeout;
		    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		    int timeoutSocket = 5000;
		    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		    DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		    BasicHttpResponse httpResponse = null;
		
			httpResponse = (BasicHttpResponse)  httpClient.execute(httpPost);
		    isReachable = (httpResponse.getStatusLine().getStatusCode() == 200);
		} catch (ClientProtocolException e) {
			isReachable = false;
		} catch (IOException e) {
			isReachable = false;
		}

	    test  = isReachable;
	    myHandler.post(updateRunnable);
	}
	
	public void testConnectionDOne(){
		dialog.dismiss();
		Log.v("orm", String.valueOf(test));
		Intent i;
    	if (test){
    		Users usr = Users.findById(Users.class, (long) 1);
    		if ((usr == null) || usr.user_id == null)
    			i = new Intent(SettingActivity.this, LoginActivity.class);
    		else
    			i = new Intent(SettingActivity.this, HomeActivity.class);
    		startActivity(i);
    	}
    	else{
    		runOnUiThread(new Runnable(){
    		    @Override
    		    public void run(){
    		    	dialog.dismiss();
    		    	Toast.makeText(SettingActivity.this, "Not connected to server, contact your supervisor for detail", Toast.LENGTH_SHORT).show();
    		    }
    		});
    	}		
	}

}
